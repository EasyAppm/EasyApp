package com.easyapp.net.http.callback;

import com.easyapp.net.http.entity.Body;
import com.easyapp.net.http.entity.Header;
import com.easyapp.net.http.entity.Response;
import com.easyapp.net.http.entity.Status;

public abstract class CallbackRest implements CallbackStream{
    @Override
    public final void onResponse(final Response response){
        if(response.containsBody()){
            Body body = response.getBody();
            body.convertString(new Body.ConvertCallback<String>(response){
                    @Override
                    public void onFinishConvert(String parse){
                        onResponse(parse, response.getStatus(), response.getHeader());
                    }

                    @Override
                    public void onFailureConvert(Throwable throwable){
                        onFailure(throwable);
                    }
                });
        }else{
            onResponse(null, response.getStatus(), response.getHeader());
        }
    }
    public abstract void onResponse(String body, Status status, Header header);
}
