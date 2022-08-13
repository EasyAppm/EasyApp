package com.easyapp.net.http.entity;

import com.easyapp.net.http.Method;

public class Request{

    private final String url;
    private final Method method;
    private final int timeOut;
    private final Header header;
    private final Body body;

    private Request(Builder builder){
        this.url = builder.url;
        this.method = builder.method;
        this.timeOut  = builder.timeOut;
        this.header = builder.header;
        this.body = builder.body;
    }


    public String getUrl(){
        return url;
    }

    public Method getMethod(){
        return method;
    }

    public int getTimeOut(){
        return timeOut;
    }

    public Header getHeader(){
        return header;
    }

    public Body getBody(){
        return body;
    }


    public static Builder get(String url){
        return new Builder(Method.GET, url);
    }

    public static Builder post(String url, Body body){
        return new Builder(Method.POST, url).setBody(body);
    }

    public static Builder head(String url){
        return new Builder(Method.HEAD, url);
    }

    public static Builder options(String url){
        return new Builder(Method.OPTIONS, url);
    }

    public static Builder put(String url, Body body){
        return new Builder(Method.PUT, url).setBody(body);
    }

    public static Builder patch(String url, Body body){
        return new Builder(Method.PATCH, url).setBody(body);
    }

    public static Builder delete(String url){
        return new Builder(Method.DELETE, url);
    }

    public static Builder trace(String url){
        return new Builder(Method.TRACE, url);
    }


    public static class Builder{

        private String url;
        private int timeOut = 20000;//20seg
        private Body body;
        private Header header;
        private Method method;
        
        public Builder(Request request){
            this.url = request.url;
            this.method = request.method;
            this.timeOut  = request.timeOut;
            this.header = request.header;
            this.body = request.body;
        }
        
        public Builder(String method, String url){
            this(Method.valueOf(method), url);
        }

        public Builder(Method method, String url){
            if(url == null) throw new IllegalArgumentException("url cannot be null");
            if(method == null) throw new IllegalArgumentException("method cannot be null");
            this.method = method;
            this.url = url;
        }

        public Builder setBody(Body body){
            this.body = body;
            return this;
        }

        public Builder setTimeOut(int timeOut){
            this.timeOut = timeOut;
            return this;
        }

        public Builder setHeader(Header header){
            this.header = header;
            return this;
        }

        public Request build(){
            if(method.isRequiredBody() && body == null){
                throw new IllegalArgumentException(String.format("The %s method cannot be built without a Body", method.name()));
            }
            return new Request(this);
        }
    }

}
