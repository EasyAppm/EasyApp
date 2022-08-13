package com.easyapp.net.http.entity;
import java.net.HttpURLConnection;

public final class Response{

    private final Header header;
    private final Status status;
    private final Body body;
    private HttpURLConnection connection;
    private final Request request;

    private Response(Builder builder){
        this.header = builder.header;
        this.status = builder.status;
        this.body = builder.body;
        this.connection = builder.connection;
        this.request = builder.request;
    }

    public Header getHeader(){
        return header;
    }

    public Status getStatus(){
        return status;
    }
    
    public Body getBody(){
        return body;
    }
    
    public Request getRequest(){
        return request;
    }

    public void disconnect(){
        if(isConnected()){
            connection.disconnect();
            connection = null;
        }
    }

    public boolean isConnected(){
        return connection != null;
    }
    
    public boolean containsBody(){
        return body != null;
    }

    public static class Builder{
        private Header header;
        private Status status;
        private Body body;
        private HttpURLConnection connection;
        private Request request;

        public Builder setHeader(Header header){
            this.header = header;
            return this;
        }

        public Builder setStatus(Status status){
            this.status = status;
            return this;
        }

        public Builder setBody(Body body){
            this.body = body;
            return this;
        }

        public Builder setConnection(HttpURLConnection connection){
            this.connection = connection;
            return this;
        }
        
        public Builder setRequest(Request request){
            this.request = request;
            return this;
        }

        public Response build(){
            return new Response(this);
        }

    }

}
