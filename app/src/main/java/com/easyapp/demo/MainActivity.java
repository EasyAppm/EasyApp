package com.easyapp.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.easyapp.demo.MainActivity;
import com.easyapp.zip.Zip;
import java.io.File;
import com.easyapp.util.FileUtils;
import com.easyapp.cipher.FileCipher;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import com.easyapp.lab.FileSecurity;
import com.easyapp.util.StreamUtils;
import java.io.FileInputStream;
import android.graphics.ImageDecoder;
import android.widget.ImageView;
import com.easyapp.Render;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.io.FileNotFoundException;
import android.graphics.Bitmap;

public class MainActivity extends Activity{ 

    ImageView image;

    File file1 = new File("storage/emulated/0/Pictures/City blue.jpg");

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        Render.into(image)
            .cache(Render.Cache.MAX_8MB)
            .load("https://images.dog.ceo/breeds/schnauzer-miniature/n02097047_4274.jpg", 
            new Render.Callback(){

                @Override
                public void onSuccess(Bitmap bitmap){
                    
                   setTitle(FileUtils.formatSize(bitmap.getByteCount()));
                    
                }

                @Override
                public void onFailure(Throwable throwable, int statusCode, String statusMessage){
                    toast(throwable.toString());
                    toast(statusCode + " - " + statusMessage);
                }
            });


    }



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.activitymainImageView1);
        ListView listview = findViewById(R.id.activitymainListView1);

        
        File file = new File("/storage/emulated/0/WhatsApp/Media/WhatsApp Images");

        /*ListAdapater adataper = new ListAdapater(this);
        listview.setAdapter(adataper);
        List<HashMap<String, Object>> list = new ArrayList<>();
        try{
            for(File fileIn : FileUtils.list(file)){
                HashMap<String, Object> hash = new HashMap<>();
                hash.put("key", fileIn);
                list.add(hash);

            }
        }catch(FileNotFoundException e){
            toast(e.toString());
        }
        toast(list.size() + "");
        adataper.update(list);*/

    }



    private void toast(boolean b){
        toast(String.valueOf(b));
    }

    private void toast(String message){
        Toast.makeText(MainActivity.this, message, 0).show();
    }

}
