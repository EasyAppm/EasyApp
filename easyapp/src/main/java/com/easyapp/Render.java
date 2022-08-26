package com.easyapp;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.LruCache;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.easyapp.core.TypeValidator;
import com.easyapp.net.http.Client;
import com.easyapp.net.http.entity.Response;
import com.easyapp.net.http.entity.Status;
import com.easyapp.task.SimpleTask;
import com.easyapp.util.BitmapUtils;
import com.easyapp.util.StreamUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


/**
 * Uma class simples para renderizar imagens
 * de urls ou file
 **/

public class Render{

    private final ImageView target;
    private Cache cache = Cache.DISABLE;
    private boolean skipCache;
    private boolean ignoreStatus;
    private Object signature;
    private int resLoading, resFailure;
    private int width, height;
    private Animation animation;
    private Callback callback;


    private Render(ImageView target){
        this.target = TypeValidator.argumentNonNull(target, "target cannot be null.");
    }

    public static Render into(ImageView target){
        return new Render(target);
    }

    public Render cache(Cache cache){
        this.cache = (cache == null) ? Cache.DISABLE : cache;
        return this;
    }

    public Render skipCache(boolean skipCache){
        this.skipCache = skipCache;
        return this;
    }

    public Render ignoreStatus(boolean ignoreStatus){
        this.ignoreStatus = ignoreStatus;
        return this;
    }

    public Render signature(Object signature){
        this.signature = signature;
        return this;
    }

    public Render loading(int resLoading){
        this.resLoading = resLoading;
        return this;
    }

    public Render failure(int resFailure){
        this.resFailure = resFailure;
        return this;
    }

    public Render resize(int width, int height){
        this.width = width;
        this.height = height;
        return this;
    }

    public Render animation(Animation animation){
        this.animation = animation;
        return this;
    }

    public Render clipToOutline(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 
            target.setClipToOutline(true);
        return this;
    }

    public void load(File file){
        load(file, null);
    }

    public void load(String url){
        load(url, null);
    }

    public void load(File file, Callback callback){
        String path = file.getAbsolutePath();
        target.setTag(R.string.tag_render, path);
        this.callback = callback;
        Bitmap bitmapCache = getCache(path);
        if(bitmapCache == null) new TaskLoad(this, file).execute();
        else new TaskLoad(this).executeCache(bitmapCache);
    }

    public void load(String url, Callback callback){
        target.setTag(R.string.tag_render, url);
        this.callback = callback;
        Bitmap bitmapCache = getCache(url);
        if(bitmapCache != null){
            new TaskLoad(this).executeCache(bitmapCache);
        }else{
            target.setImageResource(resLoading);
            Client.with(url)
                .get()
                .then(new com.easyapp.net.http.Callback(){
                    @Override
                    public void onResponse(Response response){
                        if(isTargetTagEquals(response.getRequest().getUrl())){
                            new TaskLoad(Render.this, response).execute();
                        }
                    }
                    @Override
                    public void onFailure(Throwable throwable){
                        new TaskLoad(Render.this).executeFailure(throwable);
                    }
                });
        }
    }

    public enum Cache{
        DISABLE(-1),
        MAX_2MB((1024*1024)*2),
        MAX_4MB(MAX_2MB, 2),
        MAX_8MB(MAX_4MB, 2),
        MAX_16MB(MAX_8MB, 2),
        MAX_32MB(MAX_16MB, 2);

        private int size;

        Cache(int size){
            this.size = size;
        }

        Cache(Cache cache, int multiply){
            this.size = cache.size * multiply;
        }
    }

    public interface Callback{
        void onSuccess(Bitmap bitmap);
        void onFailure(Throwable throwable, int statusCode, String statusMessage);
    }

    private boolean allowedCache(){
        return cache != Render.Cache.DISABLE && !skipCache;
    }

    private Bitmap getCache(Object key){
        if(!allowedCache()) return null;
        return CacheBitmap.getInstance(cache).get((signature == null) ? key : signature);
    }

    private String getTargetTag(){
        Object tag = target.getTag(R.string.tag_render);
        if(tag == null || !(tag instanceof String)){
            return "";
        }
        return (String)tag;
    }

    private boolean isTargetTagEquals(String text){
        return getTargetTag().equals(text);
    }

    private static class TaskLoad extends SimpleTask<Void, Bitmap>{

        private final Render render;
        private final Response response;
        private final File file;
        private boolean statusErro;

        public TaskLoad(Render render, Response response){
            this(render, response, null);
        }

        public TaskLoad(Render render, File file){
            this(render, null, file);
        }

        public TaskLoad(Render render){
            this(render, null, null);
        }

        private TaskLoad(Render render, Response response, File file){
            this.render = render;
            this.response = response;
            this.file = file;
            init();
        }

        private void init(){
            render.target.setImageResource(render.resLoading);
        }

        private boolean withResponse(){
            return response != null;
        }

        private boolean withFile(){
            return file != null;
        }

        public void executeCache(Bitmap bitmap){
            onResultTask(bitmap);
            onFinally();
        }

        public void executeFailure(Throwable throwable){
            onFailureTask(throwable);
            onFinally();
        }

        @Override
        protected Bitmap doTaskInBackground(Void[] params) throws Throwable{
            String tag = withResponse() ? response.getRequest().getUrl() : withFile() ? file.getAbsolutePath() : null;
            if(tag != null && !render.isTargetTagEquals(tag)){
                Animation anim = render.target.getAnimation();
                if(anim != null) anim.cancel();
                destroyTask();
            }
            InputStream is = null;
            Bitmap bitmap = null;
            Object signature = render.signature;
            if(withFile()){
                is = new FileInputStream(file);
                signature = (signature == null) ? file.getAbsolutePath() : signature;
            }else if(withResponse()){
                if(!render.ignoreStatus && response.getStatus().getType() != Status.Type.SUCCESS){
                    statusErro = true;
                    throw new Exception("The URL did not return a success status.");
                }
                is = response.getBody().stream();
                signature = (signature == null) ? response.getRequest().getUrl() : signature;
            }

            if((Math.max(render.width, render.height) <= 0)){
                bitmap = BitmapFactory.decodeStream(is);
            }else{
                bitmap = BitmapUtils.decodeStreamBitmap(is, render.width, render.height);
            }

            if(tag != null && !render.isTargetTagEquals(tag)){
                Animation anim = render.target.getAnimation();
                if(anim != null) anim.cancel();
                destroyTask();
            }

            if(render.allowedCache()){
                Render.CacheBitmap.getInstance(render.cache).put(signature, bitmap);
            }

            StreamUtils.close(is);
            return bitmap;
        }

        @Override
        protected void onDestroyTask(Bitmap result){
            super.onDestroyTask(result);
        }



        @Override
        protected void onResultTask(Bitmap result){
            render.target.setImageBitmap(result);
            if(render.callback != null){
                render.callback.onSuccess(result);
            }
        }

        @Override
        protected void onFailureTask(Throwable throwable){
            render.target.setImageResource(render.resFailure);
            if(render.callback != null){
                if(statusErro){
                    Status status = response.getStatus();
                    render.callback.onFailure(
                        throwable,
                        status.getCode(),
                        status.getMessage()
                    );
                }else{
                    render.callback.onFailure(throwable, -1, "");
                }
            }
        }

        @Override
        protected void onFinally(){
            if(render.animation != null){
                render.target.startAnimation(render.animation);
            }
            if(withResponse()){
                response.disconnect();
            }
        }
    }



    private static class CacheBitmap{

        private static CacheBitmap instance;
        private final LruBitmap lruBitmap;

        private CacheBitmap(Render.Cache cache){
            lruBitmap = new LruBitmap(cache.size);
        }

        public static CacheBitmap getInstance(Render.Cache cache){
            if(instance == null){
                instance = new CacheBitmap(cache);
            }
            return instance;
        }

        public void put(Object key, Bitmap bitmap){
            if(lruBitmap != null)
                lruBitmap.put(key, bitmap);
        }

        public Bitmap get(Object key){
            return (lruBitmap != null) ? lruBitmap.get(key) : null;
        }

        private class LruBitmap extends LruCache<Object, Bitmap>{

            public LruBitmap(int maxSize){
                super(maxSize);
            }

            @Override
            protected int sizeOf(Object key, Bitmap value){
                return value.getByteCount();
            }

        }
    }

}
