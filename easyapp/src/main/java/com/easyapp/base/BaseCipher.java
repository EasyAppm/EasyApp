package com.easyapp.base;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public abstract class BaseCipher{

    private final String transformation, algorithm;
    private final byte[] password, initialVector;

    public BaseCipher(String transformation, String algorithm, byte[] password, byte[] initialVector){
        if(transformation == null){
            throw new IllegalArgumentException("transformation cannot be null.");
        }
        if(algorithm == null){
            throw new IllegalArgumentException("algorithm cannot be null.");
        }
        if(password == null){
            throw new IllegalArgumentException("password cannot be null.");
        }
        this.transformation = transformation;
        this.algorithm = algorithm;
        this.password = password;
        this.initialVector = initialVector;
    }

    protected BaseCipher(String transformation, String algorithm, byte[] password){
        this(transformation, algorithm, password, null);
    }

    protected final Cipher newCipher(int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException{
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(
            mode, 
            factorySecretKey(password, algorithm),
            factoryIvParameterSpec(initialVector, cipher.getBlockSize())
        );
        return cipher;
    }

    protected final Cipher newCipherDecryptMode() throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException{
        return newCipher(Cipher.DECRYPT_MODE);
    }

    protected final Cipher newCipherEncryptMode() throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException{
        return newCipher(Cipher.ENCRYPT_MODE);
    }

    protected abstract SecretKey factorySecretKey(final byte[] password, final String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException;

    protected abstract IvParameterSpec factoryIvParameterSpec(final byte[] initialVector, final int blockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException;

}
