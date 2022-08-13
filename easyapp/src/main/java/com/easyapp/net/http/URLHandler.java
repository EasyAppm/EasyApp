package com.easyapp.net.http;

import android.net.Uri;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class URLHandler{

    private final Uri.Builder builder;
    private final List<Query> queries = new ArrayList<>();

    protected URLHandler(String url){
        if(url == null) throw new IllegalArgumentException("Url cannot be null");
        Uri uri = Uri.parse(url);
        queries.clear();
        for(String key : uri.getQueryParameterNames()){
            if(key != null && !key.isEmpty())
            queries.add(Query.create(key, uri.getQueryParameter(key)));
        }
        this.builder = uri.buildUpon().clearQuery();
    }

    public void putQuery(String key, String value){
        final int index = getQueryIndex(key);
        final Query query = Query.create(key, value);
        if(isRangeIndex(index)) queries.set(index, query);
        else queries.add(query);
    }

    public boolean removeQuery(String key){
        final int index = getQueryIndex(key);
        if(isRangeIndex(index)){
            queries.remove(index); 
            return true;
        }
        return false;
    }

    public String getQuery(String key){
        final int index = getQueryIndex(key);
        return isRangeIndex(index) ? queries.get(index).VALUE : null;
    }

    public List<Query> getQueries(){
        return queries;
    }

    public boolean existQuery(String key){
        return getQueryIndex(key) >= 0 ? true : false;
    }

    public int getQueryIndex(String key){
        for(int index = 0; index < queries.size(); index++){
            if(queries.get(index).KEY.equals(key))
                return index;
        }
        return -1;
    }

    public void clearQuery(){
        queries.clear();
        builder.clearQuery();
    }

    @Override
    public String toString(){
        saveAllQueries();
        try{
            return URLDecoder.decode(builder.build().toString(), "UTF-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        }
    }

    private boolean isRangeIndex(int number){
        return number >= 0;
    }

    private void saveAllQueries(){
        builder.clearQuery();
        for(Query query : queries)
            builder.appendQueryParameter(query.KEY, query.VALUE);
    }

    //**Query**//
    public static class Query{
        public final String KEY;
        public final String VALUE;

        public static final String SEPARATE_ENCODE = "=";
        public static final String SEPARATE = ":";

        private Query(String key, String value){
            this.KEY = key;
            this.VALUE = value;
        }

        public static Query create(String key, String value){
            return new Query(key, value);
        }

        public String getEncode(){
            return KEY + SEPARATE_ENCODE + VALUE;
        }

        public String toJson() throws JSONException{
            return new JSONObject()
                .put("key", KEY)
                .put("value", VALUE)
                .toString();
        }

        @Override
        public String toString(){
            return KEY + SEPARATE + VALUE;
        }

    }
}
