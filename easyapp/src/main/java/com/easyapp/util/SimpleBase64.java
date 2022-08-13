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
        return encodeToString(text.getBytes(), DEFAULT);
    }

    public static String encodeToString(String text, int flag){
        return encodeToString(text.getBytes(), flag);
    }

    public static String encodeToString(byte[] bytes){
        return encodeToString(bytes, DEFAULT);
    }

    public static String encodeToString(byte[] bytes, int flag){
        return Base64.encodeToString(bytes, flag);
    }

    public static byte[] encode(String text){
        return encode(text.getBytes(), DEFAULT);
    }

    public static byte[] encode(String text, int flag){
        return encode(text.getBytes(), flag);
    }

    public static byte[] encode(byte[] bytes){
        return encode(bytes, DEFAULT);
    }

    public static byte[] encode(byte[] bytes, int flag){
        return Base64.encode(bytes, flag);
    }

    //Decode

    public static String decodeToString(String text){
        return decodeToString(text.getBytes());
    }

    public static String decodeToString(String text, int flag){
        return decodeToString(text.getBytes(), flag);
    }

    public static String decodeToString(byte[] bytes){
        return decodeToString(bytes, DEFAULT);
    }

    public static String decodeToString(byte[] bytes, int flag){
        return new String(decode(bytes, flag));
    }

    public static byte[] decode(String text){
        return decode(text.getBytes());
    }

    public static byte[] decode(String text, int flag){
        return decode(text.getBytes(), flag);
    }

    public static byte[] decode(byte[] bytes){
        return decode(bytes, DEFAULT);
    }

    public static byte[] decode(byte[] bytes, int flag){
        return Base64.decode(bytes, flag);
    }

}
