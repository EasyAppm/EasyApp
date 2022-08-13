package com.easyapp.zip;

import com.easyapp.task.DirectTask;
import com.easyapp.util.StreamUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip{

    private File srcFile;
    private File destFileZip;

    public static Zip with(File srcFile){
        return new Zip(srcFile);
    }

    public Zip to(File destFileZip){
        this.destFileZip = destFileZip;
        return this;
    }

    private Zip(File srcFile){
        this.srcFile = srcFile;
    }


    public void start() throws Exception{
        if(destFileZip == null)
            throw new IllegalArgumentException("destFileZip cannot be null");
        if(srcFile == null)
            throw new IllegalArgumentException("srcFile cannot be null");
        if(destFileZip.isDirectory())
            throw new IllegalArgumentException("destFileZip cannot be a Diretory");

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destFileZip));

        if(srcFile.isDirectory()){
            zippingFolder(srcFile, zos, srcFile.getName());
        }else{
            zippingFile(srcFile, zos);
        }
        StreamUtils.close(zos);
    }

    public void startAsync(final Callback callback){
     callback.onZipStart();
        new DirectTask(){ 
            @Override
            protected void doTaskInBackground() throws Throwable{
                start();
            }
            @Override
            protected void onResutTask(){
                callback.onZipComplete();
            }
            @Override
            protected void onFailureTask(Throwable throwable){
                callback.onZipFailure(throwable);
            }
        }.execute();
    }

    private void zipping(FileInputStream fis, ZipOutputStream zos) throws IOException{
        StreamUtils.write(fis, zos);
        zos.flush();
    }


    private void zippingFile(File srcFile, ZipOutputStream zos) throws Exception{
        FileInputStream fis = new FileInputStream(srcFile);
        zos.putNextEntry(new ZipEntry(srcFile.getName()));
        zipping(fis, zos);
        zos.closeEntry();
        StreamUtils.close(fis);
    }


    private void zippingFolder(File folderToZip, ZipOutputStream zipOutputStream, String parentFolder) throws Exception{  
        File[] listFiles = folderToZip.listFiles();
        if(listFiles.length <= 0)
            zipOutputStream.putNextEntry(
                new ZipEntry(parentFolder)
            );
        for(File file : listFiles){
            final String parent = new File(parentFolder, file.getName()).getAbsolutePath();

            if(file.isDirectory()){
                zippingFolder(file, zipOutputStream, parent);
                continue;
            }
            FileInputStream fis = new FileInputStream(file);
            zipOutputStream.putNextEntry(new ZipEntry(parent));
            zipping(fis, zipOutputStream);
            zipOutputStream.closeEntry();
            StreamUtils.close(fis);
        }
    }

    public interface Callback{
        void onZipStart();
        void onZipComplete();
        void onZipFailure(Throwable throwable);
    }

}
