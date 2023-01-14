package com.easyapp.demo.database;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns;
import java.util.ArrayList;
public class MediaTeste{

    /*
     String[] projeção = new String[] {
     colunas de banco de dados de mídia para recuperar
     };
     Seleção de string = sql-where-clause-with-placeholder-variables;
     String[] selectionArgs = new String[] {
     valores-de-placeholder-variáveis
     };
     String sortOrder = sql-order-by-clause;
     */

    private final Context context;

    public MediaTeste(Context context){
        this.context = context;
    }

    public Cursor teste(){
        //Como se fosse o * 
        String[] projection = new String[] {
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.TITLE
            // media-database-columns-to-retrieve
        };
        String selection = ""; // sql-where-clause-with-placeholder-variables;
        String[] selectionArgs = new String[] {

            // values-of-placeholder-variables
        };
        String sortOrder = "";

        //projection = null;
        selectionArgs = null;



        Cursor cursor = context.getApplicationContext().getContentResolver().query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            sortOrder
        );
        return cursor;
    }


    public List<Path> getAllPaths(){
        String[] projection = new String[] {
            FileColumns._ID,
            FileColumns.TITLE,
            FileColumns.DATA,
            FileColumns.DATE_MODIFIED,
            FileColumns.MEDIA_TYPE,
            FileColumns.SIZE
            // media-database-columns-to-retrieve
        };
        String selection = ""; // sql-where-clause-with-placeholder-variables;
        String[] selectionArgs = new String[] {

            // values-of-placeholder-variables
        };
        String sortOrder = "";

        //projection = null;
        //selectionArgs = null;
        Cursor cursor = context.getApplicationContext().getContentResolver().query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            sortOrder
        );

        List<Path> list = new ArrayList<>();
        if(cursor != null){
            while(cursor.moveToNext()){
                int index = cursor.getColumnIndex("title");
                list.add(
                    new Path(cursor.getString(index))
                );
            }
        }
        
        return list;

    }

}
