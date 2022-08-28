package com.easyapp.m3u;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Set;

public class M3U{

    private final HashMap<String, String> tags;
    private final String url, description;

    public M3U(HashMap<String, String> tags, String url, String description){
        this.tags = tags;
        this.url = url;
        this.description = description;
    }

    public HashMap<String, String> getTags(){
        return tags;
    }

    public String getUrl(){
        return url;
    }

    public String getDescription(){
        return description;
    }

    public String getTag(String key){
        return getTags().get(key);
    }
    
    public Set<String> getKeysTag(){
        return tags.keySet();
    }

    public String toJson() throws JSONException{
        JSONObject jo = new JSONObject();
        jo.put("url", url);
        jo.put("description", description);
        for(String key : tags.keySet()){
            jo.put(key, tags.get(key));
        }
        return jo.toString();
    }

}
