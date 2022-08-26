package com.easyapp.net.http;

import com.easyapp.net.http.entity.Response;

public interface Callback{
    void onResponse(Response response);
    void onFailure(Throwable throwable);
}
