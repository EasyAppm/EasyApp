package com.easyapp.task;

public abstract class SimpleTask<Params, Result> extends Task<Params, Void, Result>{

    @Override
    protected final void onPostProgressTask(Void[] post){
        super.onPostProgressTask(post);
    }

    @Override
    protected final void postProgressTask(Void[] post){
        throw new UnsupportedOperationException("Impossible to post on SimpleTask.");
    }

}
