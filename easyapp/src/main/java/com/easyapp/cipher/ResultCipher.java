package com.easyapp.cipher;

import com.easyapp.core.Result;
import com.easyapp.util.SimpleBase64;

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

}
