package com.easyapp.net.http.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.easyapp.core.TypeValidator;
import com.easyapp.task.SimpleTask;
import com.easyapp.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Body{

    private final InputStream inputStream;

    private Body(InputStream inputStream){ 
        this.inputStream = TypeValidator.argumentNonNull(inputStream, "inputStream cannot be null");
    }

    public static Body create(File file) throws FileNotFoundException{
        return create(new FileInputStream(file));
    }

    public static Body create(String text){
        return create(text.getBytes());
    }

    public static Body create(byte[] bytes){
        return create(new ByteArrayInputStream(bytes));
    }

    public static Body create(InputStream inputStream){
        return new Body(inputStream);
    }

    public InputStream stream(){
        return inputStream;
    }

    public void readString(final Callback<String> callback){
        createReader(new Callback<String>(callback.response){
                @Override
                protected String onRead(InputStream inputStream) throws Throwable{
                    String text = StreamUtils.toString(inputStream);
                    StreamUtils.close(inputStream);
                    return text;
                }
                @Override
                protected void onReadSuccess(String type){
                    callback.onReadSuccess(type);
                }
                @Override
                protected void onReadFailure(Throwable throwable){
                    callback.onReadFailure(throwable);
                }
            }).execute();
    } 
    
    public void readBitmap(final Callback<Bitmap> callback){
        createReader(new Callback<Bitmap>(callback.response){
                @Override
                protected Bitmap onRead(InputStream inputStream) throws Throwable{
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    StreamUtils.close(inputStream);
                    return bitmap;
                }
                @Override
                protected void onReadSuccess(Bitmap type){
                    callback.onReadSuccess(type);
                }
                @Override
                protected void onReadFailure(Throwable throwable){
                    callback.onReadFailure(throwable);
                }
            }).execute();
    } 

    public <T> Reader<T> createReader(Callback<T> callback){
        return new Reader<T>(inputStream, callback);
    }

    public static abstract class Callback<T>{
        private final Response response;

        public Callback(Response response){
            this.response = TypeValidator.argumentNonNull(response, "Response cannot be null");
        }

        protected T onRead(InputStream inputStream) throws Throwable{
            return null;
        }

        protected abstract void onReadSuccess(T result);

        protected abstract void onReadFailure(Throwable throwable);
       
    }

    public static final class Reader<T> extends SimpleTask<Void, T>{

        private final InputStream inputStream;
        private final Callback<T> callback;

        public Reader(InputStream inputStream, Callback<T> callback){
            this.inputStream = inputStream;
            this.callback = callback;
        }

        @Override
        protected T doTaskInBackground(Void[] params) throws Throwable{
            return callback.onRead(inputStream);
        }

        @Override
        protected void onResultTask(T result){
            callback.onReadSuccess(result);
        }

        @Override
        protected void onFailureTask(Throwable throwable){
            callback.onReadFailure(throwable);
        }

        @Override
        protected void onFinally(){
            callback.response.disconnect();
        }
    } 
}
