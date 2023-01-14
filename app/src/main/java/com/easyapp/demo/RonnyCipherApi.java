package com.easyapp.demo;
import com.easyapp.cipher.AESCipher;
import com.easyapp.cipher.ResultCipher;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class RonnyCipherApi extends AESCipher{
    
    private static final byte[] KEY = "rpEasy616Ab@cat3".getBytes();
    private static final byte[] IV = "rpEasy616Ab@cat2".getBytes();
    
    private RonnyCipherApi(){
        super(KEY, IV);
    }

    @Override
    protected SecretKey factorySecretKey(byte[] password, String algorithm) throws NoSuchAlgorithmException{
        return new SecretKeySpec(password, algorithm);
    }

    @Override
    protected IvParameterSpec factoryIvParameterSpec(byte[] initialVector, int blockSize) throws NoSuchAlgorithmException{
        return new IvParameterSpec(initialVector);
    }

    public static ResultCipher decrypt(String text){
        try{
            int length = text.length();
            byte[] data = new byte[length / 2];
            for(int i = 0; i < length; i += 2){
                data [i / 2] = (byte) ((Character.digit(text.charAt(i), 16) << 4)+ Character.digit(text.charAt(i + 1), 16));
            }
            return new ResultCipher(
                new RonnyCipherApi().newCipherDecryptMode().doFinal(data)
            );
        }catch(Throwable t){
            return new ResultCipher(t);
        }
    }
}
