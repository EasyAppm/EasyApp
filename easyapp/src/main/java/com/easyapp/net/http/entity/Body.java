package com.easyapp.net.http.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.easyapp.task.SimpleTask;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.easyapp.util.StreamUtils;

public class Body{

    public static abstract class ConvertCallback<T>{
        private final Response response;
        public ConvertCallback(Response response){
            this.response = response;
        }
        public abstract void onFinishConvert(T type);
        public abstract void onFailureConvert(Throwable throwable);
    }

    private final InputStream inputStream;

    private Body(InputStream inputStream){ 
        if(inputStream == null){
            throw new IllegalArgumentException("inputStream cannot be null");
        }
        this.inputStream = inputStream;
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

    public void convertBitmap(ConvertCallback<Bitmap> onParseListener){
        new ConvertTask<Bitmap>(onParseListener){
            @Override
            protected Bitmap doTaskInBackground(InputStream[] params) throws Throwable{
                return BitmapFactory.decodeStream(params[0]);
            }
        }.execute(inputStream);
    }

    public void convertString(ConvertCallback<String> onParseListener){
        new ConvertTask<String>(onParseListener){
            @Override
            protected String doTaskInBackground(InputStream[] params) throws Throwable{
                String body = StreamUtils.toString(params[0]);
                StreamUtils.close(params[0]);
                return body;
            }
        }.execute(inputStream);
    }

    private abstract class ConvertTask<T> extends SimpleTask<InputStream, T>{

        protected ConvertCallback<T> onParseListener;

        protected ConvertTask(ConvertCallback<T> onParseListener){
            this.onParseListener = onParseListener;
        }
        @Override
        protected final void onFailureTask(Throwable throwable){
            onParseListener.onFailureConvert(throwable);
        }
        @Override
        protected final void onResultTask(T result){
            onParseListener.onFinishConvert(result);
        }
        @Override
        protected final void onFinally(){
            onParseListener.response.disconnect();
        }
    }

}
