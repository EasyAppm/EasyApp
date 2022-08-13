package com.easyapp.core;

public class TypeValidator{

    public static <T> T argumentNonNull(T type){
        return argumentNonNull(type, null);
    }

    public static <T> T argumentNonNull(T type, String menssage){
        if(type == null){
            throw new IllegalArgumentException(menssage);
        }
        return type;
    }

    public static <T> T nonNull(T type){
        return nonNull(type, null);
    }

    public static <T> T nonNull(T type, String menssage){
        if(type == null){
            throw new NullPointerException(menssage);
        }
        return type;
    }

}
