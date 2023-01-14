package com.easyapp.cipher;

import com.easyapp.core.Result;
import com.easyapp.util.SimpleBase64;
import java.io.UnsupportedEncodingException;

public class ResultCipher extends Result<byte[], Throwable>{

    public ResultCipher(byte[] data){
        super(data);
    }
    public ResultCipher(Throwable throwable){
        super(throwable);
    }

    public String getDataBase64(int flag){
        return SimpleBase64.encodeToString(getData(), flag);
    }

    public String getDataBase64(){
        return getDataBase64(SimpleBase64.DEFAULT);
    }

    public int length(){
        return isSuccess() ? getData().length : -1;
    }

    @Override
    public String toString(){
        try{
            return new String(getData(), "UTF-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        }
    }

    
}
