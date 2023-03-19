package com.easyapp.pix;

public class PixException extends Exception
{
    public PixException(String msn){
        super(msn);
    }

    public PixException(Throwable cause){
        super(cause);
    }

    public PixException(String msn, Throwable cause){
        super(msn, cause);
    }

}
