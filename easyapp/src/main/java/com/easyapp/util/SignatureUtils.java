package com.easyapp.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;

public final class SignatureUtils{

    private SignatureUtils(){}

    public static Result fromApp(Context context, String algorithm){
        return fromPackage(context, context.getPackageName(), algorithm);
    }

    public static Result fromPath(Context context, String path, String algorithm){
        if(context == null) throw new IllegalArgumentException("context cannot be null");

        if(path == null) throw new IllegalArgumentException("path cannot be null");
        else if(path.isEmpty()) throw new IllegalArgumentException("path cannot be empty");

        if(algorithm == null) throw new IllegalArgumentException("algorithm cannot be null");
        else if(algorithm.isEmpty()) throw new IllegalArgumentException("algorithm cannot be empty");

        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(path, PackageManager.GET_SIGNATURES);

            if(pi == null){
                throw new IllegalArgumentException("Unable to get information from this path");
            }

            Signature[] signatures = pi.signatures;
            if(signatures == null || signatures.length == 0){
                throw new IllegalArgumentException("No signatures found for this path");
            }
            return new Result(signatureToAlgorithm(signatures, algorithm));
        }catch(Exception e){
            return new Result(e);
        }
    }


    public static Result fromPackage(Context context, String packageName, String algorithm){
        if(context == null) throw new IllegalArgumentException("context cannot be null");

        if(packageName == null) throw new IllegalArgumentException("packageName cannot be null");
        else if(packageName.isEmpty()) throw new IllegalArgumentException("packageName cannot be empty");

        if(algorithm == null) throw new IllegalArgumentException("algorithm cannot be null");
        else if(algorithm.isEmpty()) throw new IllegalArgumentException("algorithm cannot be empty");

        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            if(pi == null){
                throw new IllegalArgumentException("Unable to get information from this packageName");
            }

            Signature[] signatures = pi.signatures;
            if(signatures == null || signatures.length == 0){
                throw new IllegalArgumentException("No signatures found for this packageName");
            }
            return new Result(signatureToAlgorithm(signatures, algorithm));
        }catch(Exception e){
            return new Result(e);
        }
    }

    private static byte[] signatureToAlgorithm(Signature[] signatures, String algorithm) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for(Signature signature : signatures){
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(signature.toByteArray());
            baos.write(md.digest());
        }
        byte[] bytes = baos.toByteArray();
        StreamUtils.close(baos);
        return bytes;
    }

    public static class Result extends com.easyapp.core.Result<byte[], Exception>{

        public Result(byte[] bytes){
            super(bytes);
        }

        public Result(Exception e){
            super(e);
        }

        public String getDataInHex(){
            StringBuilder sb = new StringBuilder();
            for(byte b : getData()){
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        }
    }

}