package com.easyapp.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.easyapp.core.TypeValidator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AssetsUtils{

    //Listar todos os arquivos de uma pasta incluindo as subpastas
    public static List<File> listAllFiles(Context context, String path) throws Exception{
        List<File> list = new ArrayList<>();
        path = clearPath(path);
        String[] array = manager(context).list(path);
        if(array.length > 0){
            for(String name : array){
                list.addAll(listAllFiles(context, path + "/" + name));
            }
        }else{
            list.add(new File(path));
        }
        return list;
    }


    //Listar tudo de uma pasta
    public static List<File> listAll(Context context, String path) throws Exception{
        List<File> files = new ArrayList<>();
        for(String pathAsset : manager(context).list(path)){
            files.add(new File(pathAsset));
        }
        return files;
    }

    public static InputStream open(Context context, String path) throws IOException {
        return manager(context).open(path);
    }

    public static boolean isFolder(Context context, String path){
        return !isFile(context, path);
    }

    public static boolean isFile(Context context, String path){
        try{
            StreamUtils.close(openFile(context, path));
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static InputStream openFile(Context context, String path) throws IOException{
        return manager(context).open(clearPath(path));
    }

    private static AssetManager manager(Context context){
        return TypeValidator.argumentNonNull(
                context,
                "Context cannot be null"
        ).getAssets();
    }

    private static String clearPath(String path){
        if(path == null){
            throw new IllegalArgumentException("Path cannot be null");
        }else if(path.isEmpty()){
            throw new IllegalArgumentException("Path cannot be empty");
        }
        return path.startsWith("/") ? path.substring(1) : path;
    }

}
