package com.easyapp.demo;

//package com.cfscine.nubia;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Nubia devs
 * author: Carlos Eduardo
 * date: 06/08/22
 * -----------------------
 * Responsavel pela leitura 
 * sincrona e assincrona dos JSON
 * dentro dos assets.
 **/

public class JsonData{

    private Activity activity;

    private JsonData(Activity activity){
        this.activity = activity;
    }

    public static JsonData newInstance(Activity activity){
        return new JsonData(activity);
    }

    private JSONArray get(String name, int max) throws Exception{
        JSONArray jsonArray = new JSONArray();
        BufferedReader br = new BufferedReader(
            new InputStreamReader(activity.getAssets().open(name))
        );
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            sb.append(line);
        }try{
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        JSONArray ja = new JSONArray(sb.toString());
        if(max <= 0) return ja;
        for (int i = 0; i < max; i++) {
            try{
            JSONObject jo = ja.getJSONObject(i);
            if(jo == null) continue;
            else jsonArray.put(jo);
            }catch(Exception e){}
        }
        return jsonArray;
    }
    
    public String getMovies() throws Exception{
        return getMovies(0);
    }
    
    public String getSeries() throws Exception{
        return getSeries(0);
    }
    
    public String getChannels() throws Exception{
        return getChannels(0);
    }

    public String getMovies(int max) throws Exception{
        return get(Names.MOVIES, max).toString();
    }

    public String getSeries(int max) throws Exception{
        return get(Names.SERIES, max).toString();
    }

    public String getChannels(int max) throws Exception{
        return get(Names.CANNELS, max).toString();
    }

    public void moviesAsync(Callback callback){
        new ThreadRead(callback).start(Names.MOVIES);
    }

    public void seriesAsync(Callback callback){
        new ThreadRead(callback).start(Names.SERIES);
    }

    public void channelsAysnc(Callback callback){
        new ThreadRead(callback).start(Names.SERIES);
    }

    public static interface Callback{
        void onSuccess(String json);
        void onFailure(Throwable t);
    }

    public static interface Names{
        String
        MOVIES = "filmes.json",
        SERIES = "series.json",
        CANNELS = "canais.json";
    }


    private class ThreadRead extends Thread{

        private String name;
        private final Callback callback;

        public ThreadRead(Callback callback){
            this.callback = callback;
        }

        @Override
        public void run(){
            try{
                String json = get(name, 0).toString();
                postSuccess(json);
            }catch(Exception e){
                postFailure(e);
            }
        }

        public void start(String name){
            this.name = name;
            start();
        }

        private void postSuccess(final String json){
            new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run(){
                        callback.onSuccess(json);
                    }
                });
        }

        private void postFailure(final Throwable t){
            new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run(){
                        callback.onFailure(t);
                    }
                });
        }
    }
}
