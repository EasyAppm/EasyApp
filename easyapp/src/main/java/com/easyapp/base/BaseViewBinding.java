package com.easyapp.base;

import android.view.View;

public abstract class BaseViewBinding{

    protected View rootView;

    public BaseViewBinding(View rootView){
        this.rootView = rootView;
        initViews();
    }

    public View getRootView(){
        return rootView;
    }

    protected abstract void initViews();

    protected final <T extends View> T find(int id){
        return rootView.findViewById(id);
    }

}
