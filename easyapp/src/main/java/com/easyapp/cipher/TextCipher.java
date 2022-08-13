package com.easyapp.cipher;

public class TextCipher extends AESCipher{

    private TextCipher(String key){
        super(key.getBytes());
    }

    /**Encrypt**/

    public static ResultCipher encrypt(String key, String text){
        return encrypt(key, text.getBytes());
    }

    public static ResultCipher encrypt(String key, byte[] bytes){
        if(key == null){
            return new ResultCipher(
                new IllegalArgumentException("key cannot be null")
            );
        }
        if(bytes == null){
            return new ResultCipher(
                new IllegalArgumentException("bytes cannot be null")
            );
        }
        try{
            return new ResultCipher(
                new TextCipher(key).newCipherEncryptMode().doFinal(bytes)
            );
        }catch(Throwable t){
            return new ResultCipher(t);
        }
    }

    /**Decrypt**/

    public static ResultCipher decrypt(String key, String text){
        return decrypt(key, text.getBytes());
    }

    public static ResultCipher decrypt(String key, byte[] bytes){
        if(key == null){
            return new ResultCipher(
                new IllegalArgumentException("key cannot be null")
            );
        }
        if(bytes == null){
            return new ResultCipher(
                new IllegalArgumentException("bytes cannot be null")
            );
        }
        try{
            return new ResultCipher(
                new TextCipher(key).newCipherDecryptMode().doFinal(bytes)
            );
        }catch(Throwable t){
            return new ResultCipher(t);
        }
    }


}
