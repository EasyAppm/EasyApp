package com.easyapp.cipher;

import com.easyapp.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class FileCipher extends AESCipher{

    private FileCipher(String key){
        super(key.getBytes());
    }

    /**Encrypt**/


    public static void encrypt(String key, File file, File fileEncrypt) throws FileNotFoundException, InvalidAlgorithmParameterException, NoSuchPaddingException, IOException, NoSuchAlgorithmException, InvalidKeyException{
        encrypt(key, null, file, fileEncrypt);
    }

    public static void encrypt(String key, byte[] firstBytes, File file, File fileEncrypt) throws FileNotFoundException, InvalidAlgorithmParameterException, NoSuchPaddingException, IOException, NoSuchAlgorithmException, InvalidKeyException{
        encrypt(key, firstBytes, new FileInputStream(file), new FileOutputStream(fileEncrypt));
    }

    public static void encrypt(String key, String text, File fileEncrypt) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, IOException, NoSuchAlgorithmException{
        encrypt(key, null, text, fileEncrypt);
    }

    public static void encrypt(String key, byte[] firstBytes, String text, File fileEncrypt) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, IOException, NoSuchAlgorithmException{
        encrypt(key, firstBytes, text, new FileOutputStream(fileEncrypt));
    }

    public static void encrypt(String key, byte[] firstBytes, String text, OutputStream outputStream) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException{ 
        if(text == null) throw new IllegalArgumentException("text cannot be null");
        if(outputStream == null) throw new IllegalArgumentException("outputStream cannot be null");

        final Cipher CIPHER = new FileCipher(key).newCipherEncryptMode();
        if(firstBytes != null){
            outputStream.write(firstBytes);
        }
        outputStream.write(CIPHER.doFinal(text.getBytes()));
        outputStream.flush();
        StreamUtils.close(outputStream);
    }


    public static void encrypt(String key, byte[] firstBytes, InputStream inputStream, OutputStream outputStream) throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException{

        if(inputStream == null){
            throw new IllegalArgumentException("InputStream cannot be null.");
        }

        if(outputStream == null){
            throw new IllegalArgumentException("OutputStream cannot be null.");
        }

        final Cipher CIPHER = new FileCipher(key).newCipherEncryptMode();

        CipherInputStream cis = new CipherInputStream(inputStream, CIPHER);

        if(firstBytes != null){
            outputStream.write(firstBytes);
        }
        StreamUtils.write(cis, outputStream);
        outputStream.flush();
        StreamUtils.close(cis, outputStream);
    }

    /**Decrypt**/

    public static void decrypt(String key, String text, File fileEncrypt) throws FileNotFoundException, InvalidAlgorithmParameterException, NoSuchPaddingException, IOException, NoSuchAlgorithmException, InvalidKeyException{
        if(text == null){
            throw new IllegalArgumentException("text cannot be null");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(text.getBytes());
        FileOutputStream fos = new FileOutputStream(fileEncrypt);
        decrypt(key, 0, bais, fos);
        StreamUtils.close(bais, fos);
    }

    public static void decrypt(String key, File fileEncrypt, File fileDecrypt) throws FileNotFoundException, InvalidAlgorithmParameterException, NoSuchPaddingException, IOException, NoSuchAlgorithmException, InvalidKeyException{
        decrypt(key, 0, fileEncrypt, fileDecrypt);
    }

    public static void decrypt(String key, long skipBytes, File fileEncrypt, File fileDecrypt) throws FileNotFoundException, InvalidAlgorithmParameterException, NoSuchPaddingException, IOException, NoSuchAlgorithmException, InvalidKeyException{
        decrypt(key, skipBytes, new FileInputStream(fileEncrypt), new FileOutputStream(fileDecrypt));  
    }


    public static ResultCipher decrypt(String key, File fileEncrypt){
        return decrypt(key, 0, fileEncrypt);
    }

    public static ResultCipher decrypt(String key, long skipBytes, File fileEncrypt){
        try{
            return decrypt(key, new FileInputStream(fileEncrypt));
        }catch(Throwable t){
            return new ResultCipher(t);
        }
    }

    public static ResultCipher decrypt(String key, InputStream inputStream){
        return decrypt(key, 0, inputStream);
    }


    public static ResultCipher decrypt(String key, long skipBytes, InputStream inputStream){ 
        if(inputStream == null) {
            return new ResultCipher(new IllegalArgumentException("inputStream cannot be null"));
        }
        final ResultCipher result;
        CipherInputStream cis = null;
        try{
            final Cipher CIPHER = new FileCipher(key).newCipherDecryptMode();
            if(skipBytes > 0) inputStream.skip(skipBytes);
            cis = new CipherInputStream(inputStream, CIPHER);
            result = new ResultCipher(StreamUtils.toBytes(cis));
        }catch(Throwable t){
            result = new ResultCipher(t);
        }finally{
            StreamUtils.close(cis);
        }
        return result;
    }


    public static void decrypt(String key, long skipBytes, InputStream inputStream, OutputStream outputStream) throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException{

        if(inputStream == null){
            throw new IllegalArgumentException("InputStream cannot be null.");
        }
        if(outputStream == null){
            throw new IllegalArgumentException("OutputStream cannot be null.");
        }
        final Cipher CIPHER = new FileCipher(key).newCipherDecryptMode();
        inputStream.skip(skipBytes);
        CipherInputStream cis = new CipherInputStream(inputStream, CIPHER);
        StreamUtils.write(cis, outputStream);
        StreamUtils.close(cis, outputStream);
    }

}
