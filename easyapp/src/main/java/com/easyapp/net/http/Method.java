package com.easyapp.net.http;

public enum Method{
    GET,
    POST,
    HEAD,
    OPTIONS,
    PUT,
    PATCH,
    DELETE,
    TRACE;
    public boolean isRequiredBody(){
        switch(this){
            case POST:
            case PUT:
            case PATCH:
                return true;
            default:
                return false;
        }
    }
}
