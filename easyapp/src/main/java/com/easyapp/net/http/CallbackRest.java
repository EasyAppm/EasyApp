package com.easyapp.net.http;

import com.easyapp.net.http.entity.Body;
import com.easyapp.net.http.entity.Header;
import com.easyapp.net.http.entity.Response;
import com.easyapp.net.http.entity.Status;

public abstract class CallbackRest implements Callback{
    @Override
    public final void onResponse(final Response response){
        if(response.containsBody()){
            response.getBody()
                .readString(new Body.Callback<String>(response){
                    @Override
                    protected void onReadSuccess(String result){
                        onResponse(result, response.getStatus(), response.getHeader());
                    }

                    @Override
                    protected void onReadFailure(Throwable throwable){
                        onFailure(throwable);
                    }
                });
        }else{
            onResponse(null, response.getStatus(), response.getHeader());
        }
    }
    public abstract void onResponse(String body, Status status, Header header);
}
