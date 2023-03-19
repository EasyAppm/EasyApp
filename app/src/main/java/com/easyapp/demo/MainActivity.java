package com.easyapp.demo;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.easyapp.demo.utils.StorageProvider;
import com.easyapp.task.Task;
import com.easyapp.timer.OnUpdateTimeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.easyapp.util.SignatureUtils;

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
            SignatureUtils.fromPath(this, getPackageCodePath(), "SHA-256").getDataInHex();
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
