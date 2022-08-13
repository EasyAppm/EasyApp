package com.easyapp.zip;

import com.easyapp.task.DirectTask;
import com.easyapp.util.FileUtils;
import com.easyapp.util.StreamUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZip{

    private File srcFileZip;
    private File destFileDirectory;

    public static UnZip with(File srcFileZip){
        return new UnZip(srcFileZip);
    }

    public UnZip to(File destFileDirectory){
        this.destFileDirectory = destFileDirectory;
        return this;
    }

    private UnZip(File srcFileZip){
        this.srcFileZip = srcFileZip;
    }


    public void start() throws Exception{
        if(destFileDirectory == null)
            throw new IllegalArgumentException("destFileDirectory cannot be null");
        if(srcFileZip == null)
            throw new IllegalArgumentException("srcFileZip cannot be null");
        if(destFileDirectory.isFile())
            throw new IllegalArgumentException("destFileDirectory cannot be a File");
        unZipping(srcFileZip, destFileDirectory);
    }
    
    public void startAsync(final Callback callback){
        callback.onUnZipStart();
        new DirectTask(){ 
            @Override
            protected void doTaskInBackground() throws Throwable{
                start();
            }
            @Override
            protected void onResutTask(){
                callback.onUnZipComplete();
            }
            @Override
            protected void onFailureTask(Throwable throwable){
                callback.onUnZipFailure(throwable);
            }
        }.execute();
    }
    

    private void writeUnZip(ZipInputStream zis, File destFile) throws Exception{
        FileOutputStream fos = new FileOutputStream(destFile);
        StreamUtils.write(zis, fos);
        StreamUtils.close(fos);
    }

    private void unZipping(File fileZip, File fileDest) throws Exception {

        if (!fileDest.exists()) {
            fileDest.mkdir();
        }

        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry;

        while ((zipEntry = zipInputStream.getNextEntry()) != null) {

            String filePath = fileDest.getAbsolutePath() 
                + File.separator + zipEntry.getName();

            if (zipEntry.isDirectory()) {
                new File(filePath).mkdirs();

            } else {
                FileUtils.createFile(new File(filePath));
                writeUnZip(zipInputStream, new File(filePath));
            }

            zipInputStream.closeEntry();

        }
        zipInputStream.close();
    }
    
    public interface Callback{
        void onUnZipStart();
        void onUnZipComplete();
        void onUnZipFailure(Throwable throwable);
    }

}

