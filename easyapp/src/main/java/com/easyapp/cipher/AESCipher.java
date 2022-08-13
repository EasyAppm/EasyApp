package com.easyapp.cipher;

import com.easyapp.base.BaseCipher;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class AESCipher extends BaseCipher{

    protected AESCipher(byte[] password){
        this(password, password);
    }

    protected AESCipher(byte[] password, byte[] initialVector){
        super("AES/CBC/PKCS5Padding", "AES", password, Objects.requireNonNull(initialVector, "initialVector cannot be null."));
    }

    @Override
    protected SecretKey factorySecretKey(byte[] password, String algorithm) throws NoSuchAlgorithmException{
        return new SecretKeySpec(getHashMD5(password), algorithm);
    }

    @Override
    protected IvParameterSpec factoryIvParameterSpec(byte[] initialVector, int blockSize) throws NoSuchAlgorithmException{
        return new IvParameterSpec(getHashMD5(initialVector));
    }

    protected byte[] getHashMD5(byte[] bytes) throws NoSuchAlgorithmException{
        return MessageDigest.getInstance("MD5").digest(bytes);
    }

}
