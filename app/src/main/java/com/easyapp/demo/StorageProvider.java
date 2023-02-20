package com.easyapp.demo;
import android.net.Uri;
import android.content.Context;
import java.util.List;
import android.database.Cursor;
import android.content.ContentResolver;
import android.provider.MediaStore;

public class StorageProvider{

    private final Context context;
    
    private Volume volume = Volume.EXTERNAL;

    public StorageProvider(Context context){
        this.context = context;
    }
    
    public StorageProvider setVolume(Volume volume){
        this.volume = volume;
        return this;
    }

    //Listar arquivos
    public List<Archive> listDownload(){
        Cursor cursor = query(MediaStore.Downloads.getContentUri(volume.toLowerCase()));
        

    }

    private Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        return context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private Cursor query(Uri uri){
        return query(uri, null, null, null, null);
    }
    
    
    //Helper
    private interface ReadCursor{
        boolean onNextCursor(Cursor cursor);
    }

    //Volume
    public enum Volume{
        EXTERNAL, INTERNAL;
        public String toLowerCase(){
            return name().toLowerCase();
        }
    }

    //Model
    public static class Archive{
        public String name;
        public Long size;
        public Uri data;

        public Archive(String name, Long size, Uri data){
            this.name = name;
            this.size = size;
            this.data = data;
        }
    }

}
