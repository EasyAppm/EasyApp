package com.easyapp.net;
import com.easyapp.core.TypeInstance;
import com.easyapp.task.Task;
import com.http.ceas.entity.Response;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import com.http.ceas.core.HttpClient;
import com.http.ceas.callback.HttpCallback;

public class DownloadArchive{

    private final String url;
    private final DownloadTask downloadTask = new DownloadTask();
    private boolean stop;

    private DownloadArchive(String url){
        this.url = url;
    }

    public static DownloadArchive from(String url){
        return new DownloadArchive(url);
    }

    public DownloadArchive to(File file){
        downloadTask.setFile(file);
        return this;
    }


    public void start(final Callback callback){
        downloadTask.setCallback(callback);
        HttpClient.with(url)
        .get()
            .then(new HttpCallback(){

                @Override
                public Runnable onResponse(Response response) throws Exception{
                    callback.onDownloadStart();
                    downloadTask.execute(response);
                    return null;
                }

                @Override
                public void onFailure(Exception e){
                    callback.onDownloadInterrupted(e);
                }

            
        });
       /* Client.with(url)
            .useTimeOut(3600000)
            .get(Header.builder()
            .put("Connection", "keep-alive")
            .build())
            .then(new com.easyapp.net.http.Callback(){
                @Override
                public void onResponse(Response response){
                    callback.onDownloadStart();
                    downloadTask.execute(response);
                }

                @Override
                public void onFailure(Throwable throwable){
                    ;
                }
            });*/
    }
    
    public void stop(){
        stop = true;
    }


    public interface Callback{
        void onDownloadStart();
        void onDownloadProgress(long currentBytes, long maxBytes, float progress);
        void onDownloadComplete();
        void onDownloadInterrupted(Throwable throwable);
    }

    private static class DownloadTask extends Task<Response, Long, Void>{

        private File file;
        private Callback callback;

        
        @Override
        protected Void doTaskInBackground(Response[] params) throws Throwable{
            Response response = params [0];
            if(response.isSuccessful()){
                InputStream inputStream = response.body().toStream();
                long total = TypeInstance.toLong(response.headers().getContentLength());
                long progress = 0;
                FileOutputStream fos = new FileOutputStream(file);
                BufferedInputStream is = new BufferedInputStream(inputStream);
                byte[] buffer = new byte[4096 * 2];
                int byteRead;
                while((byteRead = is.read(buffer)) != -1){
                    progress += byteRead;
                    fos.write(buffer, 0, byteRead);
                    postProgressTask(progress, total);
                }
                fos.flush();
                try{
                    is.close();
                    fos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                throw new UnsupportedOperationException(response.status().message());
            }
            return null;
        }

        @Override
        protected void onResultTask(Void result){
            callback.onDownloadComplete();
        }

        @Override
        protected void onFailureTask(Throwable throwable){
            callback.onDownloadInterrupted(throwable);
        }

        @Override
        protected void onPostProgressTask(Long[] post){
            long currentBytes = post[0];
            long maxBytes = post[1];
            callback.onDownloadProgress(currentBytes, maxBytes,  ((float)currentBytes / (float)maxBytes)*100f);
        }
        
        
        public void setFile(File file){
            this.file = file;
        }

        public File getFile(){
            return file;
        }
        
        public void setCallback(Callback callback){
            this.callback = callback;
        }

        public Callback getCallback(){
            return callback;
        }
        

    }

}
