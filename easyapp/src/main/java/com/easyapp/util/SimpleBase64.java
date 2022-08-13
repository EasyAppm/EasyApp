package com.easyapp.util;

import android.util.Base64;

public class SimpleBase64 extends Base64{

    //Encode

    public static String encodeToString(String text){
        return encodeToString(text.getBytes(), DEFAULT);
    }

    public static String encodeToString(String text, int flag){
        return Base64.encodeToString(text.getBytes(), flag);
    }

    public static byte[] encode(String text, int flag){
        return encode(text.getBytes(), flag);
    }

    public static byte[] encode(String text){
        return encode(text, DEFAULT);
    }


    //Decode

    public static String decodeToString(String encodedText){
        return decodeToString(encodedText, DEFAULT);
    }

    public static String decodeToString(String encodedText, int flag){
        return new String(decode(encodedText, flag));
    }

    public static byte[] decode(String text, int flag){
        return decode(text.getBytes(), flag);
    }

    public static byte[] decode(String text){
        return decode(text, DEFAULT);
    }

}
