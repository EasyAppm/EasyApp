package com.easyapp.demo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.easyapp.util.StreamUtils;

public class ObjectDataBase<T> implements Closeable{

    private final DataBase dataBase;

    public ObjectDataBase(Context context, Class<T> classType){
        this.dataBase = new DataBase(context, classType.getSimpleName());
    }

    public boolean save(T type){
        try{
            saveOrThrows(type);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public void saveOrThrows(T type) throws Exception{
        dataBase.saveData(serialize(type));
    }

    public List<T> getAll(){
        try{
            return getAllOrThrows();
        }catch(Exception e){
            return null;
        }
    }

    public List<T> getAllOrThrows() throws Exception{
        return findAll(dataBase.getData());
    }

    

    @Override
    public void close(){
        dataBase.close();
    }


    //Serialization

    private byte[] serialize(T type) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        byte[] bytes = null;
        oos.writeObject(type);
        bytes = baos.toByteArray();
        StreamUtils.close(baos, oos);
        return bytes;
    }
    

    private T deserialize(byte[] bytes) throws Exception{
        T type = null;
        ObjectInputStream ois = new ObjectInputStream(
            new ByteArrayInputStream(bytes)
        );
        type = (T) ois.readObject();
        StreamUtils.close(ois);
        return type;
    }

    //Cursor

    private T findLast(Cursor cursor) throws Exception{
        if(cursor.moveToLast()){
            return findIndex(cursor, cursor.getPosition());
        }
        return null;
    }

    private T findFirst(Cursor cursor) throws Exception{
        if(cursor.moveToFirst()){
            return findIndex(cursor, cursor.getPosition());
        }
        return null;
    }

    private T findIndex(Cursor cursor, int index) throws Exception{
        T type = null;
        if(cursor != null || cursor.getCount() != 0){
            return deserialize(cursor.getBlob(index));
        }
        cursor.close();
        return type;
    }

    private List<T> findAll(Cursor cursor) throws Exception{
        List<T> list = new ArrayList<>();
        if(cursor != null || cursor.getCount() != 0){
            while(cursor.moveToNext()){
                int index = cursor.getColumnIndex("DATA");
                if(index >= 0 && !cursor.isNull(index)){
                    list.add(deserialize(cursor.getBlob(index)));
                }
            }
        }
        cursor.close();
        return list;
    }

    //SqlLite

    private static class DataBase extends SQLiteOpenHelper{

        private final String TABLE_NAME;

        public DataBase(Context context, String name){
            super(context, name.concat(".db"), null, 1);
            this.TABLE_NAME = name;
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( DATA CLOB )"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.delete(TABLE_NAME, null, null);
        }

        public void saveData(byte[] bytes){
            ContentValues values = new ContentValues();
            values.put("data", bytes);
            getWritableDatabase().insertOrThrow(TABLE_NAME, null, values);
        }

        public Cursor getData(){
            return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        }
    }
}
