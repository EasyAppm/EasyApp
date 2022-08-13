package com.easyapp.task;

public abstract class DirectTask extends SimpleTask<Void, Void> {

    @Override
    protected final Void doTaskInBackground(Void[] params) throws Throwable{
        doTaskInBackground();
        return null;
    }

    @Override
    protected final void onResultTask(Void result){
        onResutTask();
    }

    protected abstract void doTaskInBackground() throws Throwable;
    protected abstract void onResutTask();

}
