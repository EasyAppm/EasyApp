package com.easyapp.demo;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;

public class PhoneInfo{

    private final JSONObject jsonObject;

    public PhoneInfo(String json) throws JSONException{
        this.jsonObject = new JSONObject(json);
    }

    public long getImei(){
        try{
            return jsonObject.getLong("query");
        }catch(JSONException e){
            return 0;
        }
    }

    public long getSerial(){
        return (Long) getData("serial");
    }

    public String getSimSlots(){
        JSONObject jo = (JSONObject) getData("device_spec");
        if(jo!=null){
            try{
                return jo.getString("sim_slots");
            }catch(JSONException e){
                return null;
            }
        }
        return null;
    }


    public String getName(){
        return (String) getData("name");
    }
    
    public String getModel(){
        return (String) getData("model");
    }

    public String getManufacturer(){
        return (String) getData("manufacturer");
    }

    public String getType(){
        return (String) getData("type");
    }

    public List<String> getFrequency(){
        List<String> list = new ArrayList<>();
        try{
            JSONArray ja = (JSONArray) getData("frequency");
            for(int i = 0; i < ja.length(); i++) list.add(ja.getString(i));
            return list;
        }catch(JSONException e){
            return list;
        }
    }

    
    public boolean getBlackList(){
        JSONObject jo = (JSONObject) getData("blacklist");
        try{
            return jo.getBoolean("status");
        }catch(JSONException e){
            return false;
        }
    }
    
    private JSONObject getJsonObject(String key){
        try{
            return jsonObject.getJSONObject(key);
        }catch(JSONException e){
            return null;
        }
    }
    
    private Object getData(String key){
       JSONObject jo = getJsonObject("data");
        try{
            return jo == null ? "" : jo.get(key);
        }catch(JSONException e){
            return "";
        }
    }

}
