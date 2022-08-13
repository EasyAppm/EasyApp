package com.easyapp.net.http;

public enum Property{
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    AUTHORIZATION("Authorization"),
    ACCEPT("Accept");

    private String property;

    Property(String property){
        this.property = property;
    }

    @Override
    public String toString(){
        return property;
    }
}
