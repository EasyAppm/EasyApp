package com.easyapp.net.http.callback;

import com.easyapp.net.http.entity.Response;

public interface CallbackStream{
    void onResponse(Response response);
    void onFailure(Throwable throwable);
}
