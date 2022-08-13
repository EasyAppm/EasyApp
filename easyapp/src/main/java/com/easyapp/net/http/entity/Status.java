package com.easyapp.net.http.entity;

public class Status{

    private final int code;
    private final String message;

    public Status(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }
    
    public Type getType(){
        return Type.valueOf(code);
    }

    public static enum Type{
        INFO, SUCCESS, REDIRECTION, 
        CLIENT_ERRO, SERVER_ERRO;

        public static Type valueOf(int code){
            switch(Integer.valueOf(String.valueOf(code).substring(0, 1))){
                case 1: return Type.INFO;   
                case 2: return Type.SUCCESS;
                case 3: return Type.REDIRECTION;
                case 4: return Type.CLIENT_ERRO;
                case 5: return Type.SERVER_ERRO;
                default: throw new IllegalArgumentException("unrecognized type");
            }
        }
    }

}
