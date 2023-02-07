package com.easyapp.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.easyapp.timer.OnUpdateTimeListener;
import java.io.Serializable;
import com.easyapp.demo.database.ObjectDataBase;
import com.easyapp.demo.database.MediaTeste;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;
import java.util.Arrays;
import com.easyapp.demo.database.Path;
import java.util.List;
import com.easyapp.demo.utils.StorageProvider;
import com.easyapp.task.SimpleTask;
import com.easyapp.task.Task;
import android.widget.ListView;
import java.util.HashMap;
import java.util.ArrayList;
import android.content.ContentUris;
import android.net.Uri;
import com.http.ceas.core.HttpClient;
import com.http.ceas.callback.HttpCallback;
import com.http.ceas.entity.Response;
import com.http.ceas.callback.RestCallback;
import com.http.ceas.core.HttpHeaders;
import com.http.ceas.core.HttpStatus;
import com.http.ceas.callback.GsonCallback;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity implements OnUpdateTimeListener{

    TextView text;
    ImageView image;
    ListView listView;
    ListAdapater adapater;
    StorageProvider provider;


    @Override
    public void onBackPressed(){
        // MediaStore.Images.Media.getContentUri("hshs", 0);
        text.setText(MediaStore.Files.getContentUri("external").toString());

        List<HashMap<String, Object>> list = new ArrayList<>();
        Cursor cursor = provider.getAllImages();
        int max = cursor.getCount();
        int progress = 0;
        if(cursor != null){
            while(cursor.moveToNext()){
                int index = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                int indexData = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                String title = cursor.getString(index);
                // setTitle(cursor.getType(indexData)+"");
                HashMap<String, Object> map = new HashMap<>();

                map.put("name", cursor.getString(index));
                map.put("data", Uri.parse(cursor.getString(indexData)));
                list.add(map);
            }
        }
        adapater.update(list);

    }

    @Override
    public void onUpdateTime(long miliseconds){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.activitymainTextView1);
        image = findViewById(R.id.activitymainImageView1);
        listView = findViewById(R.id.activitymainListView1);
        provider = StorageProvider.getInstance(this);
        adapater = new ListAdapater(this);
        listView.setAdapter(adapater);

    } 


    public void click(View View){

        TypeToken<List<Pessoa>> type = new TypeToken<List<Pessoa>>(){};

        try{
            Pessoa pessoa = HttpClient.with("").get()
            .execute().body().toType(Pessoa.class);
        }catch(Exception e){}

        
        HttpClient.with("")
        .delete()
            .then(new HttpCallback(){

                @Override
                public Runnable onResponse(Response response) throws Exception{
                    //Leitura do body
                    final Pessoa pessoa = response.body().toType(Pessoa.class);
                    //Abriu uma thread na ui
                    return new Runnable(){

                        @Override
                        public void run(){
                            text.setText(pessoa.toString());
                        }

                        
                    };
                }

                @Override
                public void onFailure(Exception p1){
                }

                
            });


    }

    private class TaskCursor extends Task<Cursor, Integer, String>{



        @Override
        protected String doTaskInBackground(Cursor[] params) throws Throwable{
            StringBuilder sb = new StringBuilder();
            Cursor cursor = params [0];
            int max = cursor.getCount();
            int progress = 0;
            if(cursor != null){
                while(cursor.moveToNext()){
                    int index = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                    String title = cursor.getString(index);
                    sb.append(title).append("\n");
                    postProgressTask(progress++, max);
                }
            }
            return sb.toString();
        }

        @Override
        protected void onResultTask(String result){
            text.setText(result);
        }

        @Override
        protected void onPostProgressTask(Integer[] post){
            MainActivity.this.setTitle(post [0] + " _ " + post [1]);
        }



        @Override
        protected void onFailureTask(Throwable throwable){
            text.setText(throwable.toString());
        }


    }


    private static class Teste implements Serializable{

        static final long serialVersionUID = 999;

        String name;
        int age;

        public Teste(String name, int age){
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString(){
            return name + "_" + age;
        }

        public void teste(){

        }


    }



}
