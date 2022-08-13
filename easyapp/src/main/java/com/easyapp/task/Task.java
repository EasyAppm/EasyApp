package com.easyapp.task;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Task<Params, Post, Result> implements Runnable{

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Params[] params;
    private boolean destroyed, running;

    @Override
    public final void run(){
        try{
            resultTask(doTaskInBackground(params));
        }catch(Throwable t){
            failureTask(t);
        }finally{
            running = false;
            postFinally();
        }
    }

    public final void execute(Params... params){
        if(destroyed){
            throw new IllegalThreadStateException("the task has already been destroyed");
        }
        this.params = params;
        this.running = true;
        onPrepareTask();
        executorService.execute(this);
    }

    public boolean isRunning(){
        return running;
    }


    public void destroyTask(){
        this.destroyed = true;
    }


    public boolean isTaskDestroyed(){
        return destroyed;
    }


    /**
     * Chamado antes da execução
     **/
    protected void onPrepareTask(){}

    /**
     * Postar progresso na thread principal
     **/
    protected void onPostProgressTask(Post... post){}

    /**
     * Obter resultado quando a task for destruida
     **/
    protected void onDestroyTask(Result result){}

    /**
     * Finally
     **/
    protected void onFinally(){}

    /**
     * Realizar task
     **/
    protected abstract Result doTaskInBackground(Params... params) throws Throwable;

    /**
     * Obter resultado da task
     **/
    protected abstract void onResultTask(Result result);

    /**
     * Capturar Exceptions
     **/
    protected abstract void onFailureTask(Throwable throwable);



    protected void postProgressTask(final Post... post){
        handler.post(new Runnable(){
                @Override
                public void run(){
                    onPostProgressTask(post);
                }
            });
    }

    private final void resultTask(final Result result){
        handler.post(new Runnable(){
                @Override
                public void run(){
                    if(destroyed)
                        onDestroyTask(result);
                    else
                        onResultTask(result);
                }
            });
    }

    private final void failureTask(final Throwable throwable){
        handler.post(new Runnable(){
                @Override
                public void run(){
                    onFailureTask(throwable);
                }
            });
    }

    private final void postFinally(){
        handler.post(new Runnable(){
                @Override
                public void run(){
                    onFinally();
                }
            });
    }

}

