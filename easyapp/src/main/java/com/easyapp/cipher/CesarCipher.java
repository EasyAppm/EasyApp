package com.easyapp.cipher;

import java.util.ArrayList;
import java.util.List;

public final class CesarCipher{

    private final static int DECRYPT = 0, ENCRYPTY = 1;

    public static String encrypt(String text, int key){
        return engine(text.toCharArray(), key, ENCRYPTY);
    }

    public static String decrypt(String textEncrypt, int key){
        return engine(textEncrypt.toCharArray(), key, DECRYPT);
    }

    public static List<String> brutalForce(String textEncrypt, int max){
        return brutalForce(textEncrypt, 0, max);
    }

    public static List<String> brutalForce(String textEncrypt, int min, int max){
        if(min > max){
            throw new IllegalArgumentException("min value cannot be > max");
        } 
        List<String> list = new ArrayList<>();
        for(int key = min; key <= max; key++){
            list.add(engine(textEncrypt.toCharArray(), key, DECRYPT));
        }
        return list;
    }

    private static String engine(final char[] characters, int key, int mode){
        for(int i = 0; i < characters.length; i++){
            if(mode == ENCRYPTY)
                characters[i] += key;
            else if(mode == DECRYPT)
                characters[i] -= key;
        }
        return new String(characters);
    }
}
