package com.easyapp.util;

import android.util.Base64;

public class SimpleBase64{
    
    public static final int CRLF = 4;

    public static final int DEFAULT = 0;

    public static final int NO_CLOSE = 16;

    public static final int NO_PADDING = 1;

    public static final int NO_WRAP = 2;

    public static final int URL_SAFE = 8;

    //Encode

    public static String encodeToString(String text){
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
    }

    public static String encodeToString(String text, int flag){
        return Base64.encodeToString(text.getBytes(), flag);
    }
    
    public static String encodeToString(byte[] bytes, int flag){
        return  Base64.encodeToString(bytes, flag);
    }

    public static byte[] encode(String text, int flag){
        return encode(text.getBytes(), flag);
    }
    
    public static byte[] encode(byte[] bytes, int flag){
        return Base64.encode(bytes, flag);
    }

    public static byte[] encode(String text){
        return encode(text, Base64.DEFAULT);
    }


    //Decode

    public static String decodeToString(String encodedText){
        return decodeToString(encodedText, Base64.DEFAULT);
    }

    public static String decodeToString(String encodedText, int flag){
        return new String(decode(encodedText, flag));
    }

    public static byte[] decode(String text, int flag){
        return Base64.decode(text.getBytes(), flag);
    }

    public static byte[] decode(String text){
        return Base64.decode(text, Base64.DEFAULT);
    }

}
