package com.easyapp.net.http.entity;

import com.easyapp.net.http.Property;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONException;

public class Header{

    private final Map<String, String> headers;

    private Header(Builder builder){
        this.headers = builder.headers;
    }

    public static Builder builder(){
        return new Header.Builder();
    }

    public String get(String key){
        return headers.get(key);
    }

    public boolean containsKey(String key){
        return headers.containsKey(key);
    }

    public boolean containsValue(String value){
        return headers.containsValue(value);
    }

    public String getAuth(){
        return get(Property.AUTHORIZATION.toString());
    }

    public String getContentType(){
        return get(Property.CONTENT_TYPE.toString());
    }

    public String getContentLength(){
        return get(Property.CONTENT_LENGTH.toString());
    }

    public String[] getKeys(){
        return (headers == null) ? new String[0] : headers.keySet().toArray(new String[0]);
    }

    public void clear(){
        headers.clear();
    }

    public String remove(String key){
        return headers.remove(key);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(String key : headers.keySet()){
            sb.append(key)
                .append(": ")
                .append(headers.get(key))
                .append("\n");
        }
        return sb.toString();
    }

    public String toJson(int indentSpaces) throws JSONException{
        JSONObject jo = new JSONObject();
        for(String key : headers.keySet()){
            jo.put(key, headers.get(key));
        }
        return jo.toString(indentSpaces);
    }

    public static class Builder{

        private final Map<String, String> headers = new HashMap<>();

        public Builder(){}

        public Builder(Header header){
            if(header != null)
                this.headers.putAll(header.headers);
        }

        public Builder put(String key, String header){
            headers.put(key, header);
            return this;
        }

        public Builder putAuth(Auth auth){
            return put(Property.AUTHORIZATION.toString(), auth.get());
        }

        public Builder putContentType(String header){
            return put(Property.CONTENT_TYPE.toString(), header);
        }

        public Builder putContentLength(String header){
            return put(Property.CONTENT_LENGTH.toString(), header);
        }

        public Builder putAccept(String header){
            return put(Property.ACCEPT.toString(), header);
        }

        public Header build(){
            return new Header(this);
        }
    }

}
