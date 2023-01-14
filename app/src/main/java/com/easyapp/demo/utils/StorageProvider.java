package com.easyapp.demo.utils;
import android.content.Context;
import android.database.Cursor;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.provider.MediaStore;

import static android.provider.MediaStore.Files.FileColumns;
import static android.provider.MediaStore.Images.ImageColumns;

public class StorageProvider{

    private static StorageProvider singleton;

    private final static String[] PROJECTION;

    static{
        PROJECTION = new String[]{
            FileColumns.DOCUMENT_ID,
            FileColumns._ID,
            FileColumns.RELATIVE_PATH,
            FileColumns.TITLE,
            FileColumns.DATA,
            FileColumns.DATE_MODIFIED,
            FileColumns.DISPLAY_NAME,
            FileColumns.DATE_EXPIRES
        };
    }

    private final ContentResolver contentResolver;


    private StorageProvider(Context context){
        this.contentResolver = context.getContentResolver();
    }

    public static StorageProvider getInstance(Context context){
        if(singleton == null){
            singleton = new StorageProvider(context);
        }
        return singleton;
    }

    public Cursor getAllImages(){
       
        return contentResolver.query(
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
            PROJECTION,
            null,
            null
        );
    }





}
