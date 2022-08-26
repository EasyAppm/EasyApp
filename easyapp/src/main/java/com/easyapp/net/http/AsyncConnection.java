package com.easyapp.net.http;

import com.easyapp.core.TypeValidator;
import com.easyapp.net.http.entity.Body;
import com.easyapp.net.http.entity.Header;
import com.easyapp.net.http.entity.Request;
import com.easyapp.net.http.entity.Response;
import com.easyapp.net.http.entity.Status;
import com.easyapp.task.SimpleTask;
import com.easyapp.util.StreamUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AsyncConnection{

    private final Request request;

    private AsyncConnection(Request request){
        this.request = TypeValidator.argumentNonNull(request, "request cannot be null.");
    }

    public static AsyncConnection open(Request request){
        return new AsyncConnection(request);
    }

    public void then(final Callback callback){
        new TaskConnection(callback).execute(request);
    }

    private class TaskConnection extends SimpleTask<Request, Response>{

        protected final Callback callback;
        private HttpURLConnection http;

        protected TaskConnection(Callback callback){
            this.callback = callback;
        }

        @Override
        protected final void onFailureTask(Throwable throwable){
            callback.onFailure(throwable);
        }

        @Override
        protected Response doTaskInBackground(Request[] params) throws Throwable{
            Request request = params [0];
            http = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            if(http == null){
                throw new NullPointerException("Connection null");
            }else if(http instanceof HttpsURLConnection){
                try{
                    ((HttpsURLConnection)http).setSSLSocketFactory(Security.getSocketFactory());
                    ((HttpsURLConnection)http).setHostnameVerifier(Security.getHostnameVerifier());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            final Method method = request.getMethod();
            final Header header = request.getHeader();
            final Body body = request.getBody();

            http.setConnectTimeout(request.getTimeOut());
            http.setReadTimeout(request.getTimeOut());
            http.setRequestMethod(method.name());
            http.setDoInput(method != Method.HEAD);
            http.setDoOutput(method.isRequiredBody());

            if(header != null){
                for(String key : header.getKeys())
                    http.setRequestProperty(key, header.get(key));
            }

            if(body != null){
                sendBody(body);
            }

            return new Response.Builder()
                .setHeader(getHeader())
                .setStatus(new Status(http.getResponseCode(), http.getResponseMessage()))
                .setConnection(http)
                .setBody(getBody())
                .setRequest(request)
                .build();
        }

        @Override
        protected void onResultTask(Response result){
            callback.onResponse(result);
        }

        protected Header getHeader(){
            int size = http.getHeaderFields().size();
            Header.Builder builder = Header.builder();
            for(int i = 0; i < size; i++){
                String key = http.getHeaderFieldKey(i);
                if(key != null && !key.isEmpty()) builder.put(key, http.getHeaderField(i));
            }
            return builder.build();
        } 

        private Body getBody() throws IOException{
            InputStream is = http.getErrorStream();
            if(is == null) is = http.getInputStream();
            return Body.create(is);
        }

        private void sendBody(Body body) throws Throwable{
            BufferedOutputStream bos = new BufferedOutputStream(http.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(body.stream());
            if(bis == null){
                throw new IllegalArgumentException("Request body invalid");
            }
            StreamUtils.write(bis, bos);
            StreamUtils.close(bis, bos);
        }
    }

    private static class Security{
        static SSLSocketFactory getSocketFactory() throws Exception{
            final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType){}
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType){}
                    @Override
                    public X509Certificate[] getAcceptedIssuers(){
                        return new X509Certificate[]{};
                    }
                }
            };
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        }
        static HostnameVerifier getHostnameVerifier(){
            return new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session){
                    return true;
                }
            };
        }
    }
}
