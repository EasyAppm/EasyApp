package com.easyapp.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class FileUtils{

    public static final FileFilter FILTER_FOLDERS = new FileFilter(){
        @Override
        public boolean accept(File file){
            return isFolder(file);
        } 
    };

    public static final FileFilter FILTER_FILES = new FileFilter(){
        @Override
        public boolean accept(File file){
            return isFile(file);
        } 
    };

    /**File**/

    public static boolean exists(File file){
        return file.exists();
    }

    public static boolean notExists(File file){
        return !exists(file);
    }

    public static boolean isFile(File file){
        return file.isFile();
    }

    public static boolean isFolder(File file){
        return file.isDirectory();
    }

    public static boolean isHidden(File file){
        return file.isHidden();
    }

    public static String getName(File file){
        return file.getName();
    }

    public static long getSize(File file){
        return exists(file) ? file.length() : 0L;
    }

    public static String getFormattedSize(File file){
        return formatSize(getSize(file));
    }

    public static boolean createFile(File file) throws IOException{
        createFolder(file.getParentFile()); 
        return file.createNewFile();
    }

    public static boolean createFolder(File file){
        return file.mkdirs();
    }


    public static boolean delete(File file){
        if(notExists(file)) return false;
        if(isFile(file)) return file.delete();
        else{ 
            for(File subFile : file.listFiles()){
                if(isFolder(subFile)) delete(subFile);
                else subFile.delete();
            }
        }
        return file.delete();
    }


    public static boolean copyFile(File fromFile, File toFile) throws IOException{
        createFile(toFile);
        FileInputStream fis = new FileInputStream(fromFile);
        FileOutputStream fos = new FileOutputStream(toFile);
        StreamUtils.write(fis, fos);
        StreamUtils.close(fis, fos);
        return true;
    }


    public static boolean copyFolder(File fromFile, File toFile) throws IOException{
        if(notExists(fromFile)) return false;
        if(notExists(toFile)) createFolder(toFile);

        if(isFile(fromFile)) 
            copyFile(fromFile, new File(toFile, getName(fromFile)));
        else{
            for(File subFile : fromFile.listFiles()){
                File destFile = new File(toFile, getName(subFile));
                if(isFolder(subFile)) copyFolder(subFile, destFile);
                else copyFile(subFile, destFile);
            }
        }
        return true;
    }

    public static boolean moveFile(File fromFile, File toFile) throws IOException{
        if(copyFile(fromFile, toFile)) return delete(fromFile);
        else return false;
    }


    public static boolean moveFolder(File fromFile, File toFile) throws IOException{
        if(copyFolder(fromFile, toFile)) return delete(fromFile);
        else return false;
    }

    public static byte[] fileToBytes(File file, int length) throws IOException{
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = StreamUtils.toBytes(fis, length);
        StreamUtils.close(fis);
        return bytes;
    }

    public static String fileToString(File file) throws IOException{
        FileInputStream fis = new FileInputStream(file);
        String text = StreamUtils.toString(fis);
        StreamUtils.close(fis);
        return text;
    }

    public static void write(byte[] bytes, File fileOut) throws IOException{
        FileOutputStream fos = new FileOutputStream(fileOut);
        StreamUtils.write(bytes, fos);
        StreamUtils.close(fos);
    }

    public static void write(String text, File fileOut) throws IOException{
        write(text.getBytes(), fileOut);
    }

    public static List<File> list(File file, FileFilter fileFilter) throws FileNotFoundException{
        if(isFile(file)){
            throw new IllegalArgumentException("File must be a folder");
        }
        if(notExists(file)){
            throw new FileNotFoundException();
        }
        return Arrays.asList(file.listFiles((fileFilter)));
    }

    public static List<File> list(File file) throws FileNotFoundException{
        return list(file, null);
    }


    /**String**/

    public static boolean exists(String path){
        return exists(new File(path));
    }

    public static boolean notExists(String path){
        return notExists(new File(path));
    }

    public static boolean isFile(String path){
        return isFile(new File(path));
    }

    public static boolean isFolder(String path){
        return isFolder(new File(path));
    }

    public static boolean isHidden(String path){
        return isHidden(new File(path));
    }

    public static String getName(String path){
        return getName(new File(path));
    }

    public static long getSize(String path){
        return getSize(new File(path));
    }

    public static String getFormattedSize(String path){
        return getFormattedSize(new File(path));
    }

    public static boolean createFile(String path){
        try{
            return createFile(new File(path));
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createFolder(String path){
        return createFolder(new File(path));
    }

    public static boolean delete(String path){
        return delete(new File(path));
    }

    public static boolean copyFile(String fromPath, String toPath){
        try{
            return copyFile(new File(fromPath), new File(toPath));
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyFolder(String fromPath, String toPath){
        try{
            return copyFolder(new File(fromPath), new File(toPath));
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean moveFile(String fromFile, String toFile){
        try{
            return moveFile(new File(fromFile), new File(toFile));
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean moveFolder(String fromFile, String toFile){
        try{
            return moveFolder(new File(fromFile), new File(toFile));
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] fileToBytes(String path, int length){
        try{
            return fileToBytes(new File(path), length);
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String fileToString(String path){
        try{
            return fileToString(new File(path));
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean write(byte[] bytes, String pathOut){
        try{
            write(bytes, new File(pathOut));
            return true;
        }catch(IOException e){
            return false;
        }
    }

    public static boolean write(String text, String pathOut){
        return write(text.getBytes(), pathOut);
    }

    public static List<File> list(String path, FileFilter fileFilter){
        try{
            return list(new File(path), fileFilter);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<File> list(String path){
        return list(path, null);
    }

    public static String formatSize(long size){
        if(size <= 0) return "0B";
        final String[] prefixes = new String[] { "B", "KB", "MB", "GB", "TB", "PB", "EB" };
        int indexPrefixes = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#")
            .format(size / Math.pow(1024, indexPrefixes)) + " " + prefixes [indexPrefixes];
    }
}
