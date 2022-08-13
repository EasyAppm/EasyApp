package com.easyapp.lab;

import com.easyapp.cipher.FileCipher;
import com.easyapp.core.TypeValidator;
import com.easyapp.task.DirectTask;
import com.easyapp.util.StreamUtils;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class FileSecurity{

    public static final Signature DEFAULT_SIGNATURE = FileSecurity.newSignature("FS-SANDRO-CARLOS");
    private final File targetFile, saveFile;
    private Signature signature;

    private FileSecurity(File targetFile, File saveFile){
        this.targetFile = TypeValidator.argumentNonNull(targetFile, "targetFile cannot be null");
        this.saveFile = TypeValidator.argumentNonNull(saveFile, "saveFile cannot be null");
        this.signature = DEFAULT_SIGNATURE;
    }

    public static FileSecurity create(File targetFile, File saveFile){
        return new FileSecurity(targetFile, saveFile);
    }

    public void setSignature(Signature signature){
        this.signature = TypeValidator.argumentNonNull(signature, "signature cannot be null");
    }

    public Signature getSignature(){
        return signature;
    }

    public void lock(String password) throws Exception{
        FileCipher.encrypt(password, signature.hash(), targetFile, saveFile);
    }

    public void unLock(String password) throws Exception{
        FileCipher.decrypt(password, signature.hash().length, targetFile, saveFile);
    }

    public void lockAsync(final String password, final LockCallback lockCallback){
        lockCallback.onLockStart();
        new DirectTask(){
            @Override
            protected void doTaskInBackground() throws Throwable{
                lock(password);
            }
            @Override
            protected void onResutTask(){
                lockCallback.onLockComplete();
            }
            @Override
            protected void onFailureTask(Throwable throwable){
                lockCallback.onLockFailure(throwable);
            } 
        }.execute();

    }

    public void unLockAsync(final String password, final UnLockCallback unLockCallback){
        unLockCallback.onUnLockStart();
        new DirectTask(){
            @Override
            protected void doTaskInBackground() throws Throwable{
                unLock(password);
            }
            @Override
            protected void onResutTask(){
                unLockCallback.onUnLockComplete();
            }
            @Override
            protected void onFailureTask(Throwable throwable){
                unLockCallback.onUnLockFailure(throwable);
            } 
        }.execute();
    }

    public static boolean isFileLock(File file){
        return isFileLock(file, null);
    }

    public static boolean isFileLock(File file, Signature signature){
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(file);
            byte[] hs = (signature == null ? DEFAULT_SIGNATURE : signature).hash();
            return Arrays.equals(StreamUtils.toBytes(fis, hs.length), hs);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            StreamUtils.close(fis);
        }
    }

    public static Signature newSignature(String signature){
        return newSignature(signature, null);
    }

    public static Signature newSignature(String signature, String algorithm){
        return new Signature(signature.getBytes(), algorithm);
    }

    public interface LockCallback{
        void onLockStart();
        void onLockComplete();
        void onLockFailure(Throwable throwable);
    }

    public interface UnLockCallback{
        void onUnLockStart();
        void onUnLockComplete();
        void onUnLockFailure(Throwable throwable);
    }


    public static class Signature{
        private final byte[] bytes;
        private String algorithm;

        private Signature(byte[] bytes, String algorithm){
            this.bytes = TypeValidator.nonNull(bytes);
            this.algorithm = algorithm;
        }

        public byte[] getBytes(){
            return bytes;
        }

        public String getAlgorithm(){
            return algorithm;
        }

        @Override
        public String toString(){
            return new String(getBytes());
        }

        private byte[] hash() throws NoSuchAlgorithmException{
            if(algorithm == null || algorithm.isEmpty()){
                algorithm = "MD5";
            }
            return MessageDigest.getInstance(algorithm).digest(bytes);
        }

    }

}
