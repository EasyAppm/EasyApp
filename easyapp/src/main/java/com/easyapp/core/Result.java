package com.easyapp.core;

public class Result<T, E extends Throwable>{

    private final T data;
    private final E error;

    public Result(E error){
        if(error == null){
            throw new IllegalArgumentException("error cannot be null");
        }
        this.data = null;
        this.error = error;
    }

    public Result(T data){
        if(data == null){
            throw new IllegalArgumentException("data cannot be null");
        }
        this.data = data;
        this.error = null;
    }

    @Override
    public String toString() {
        return isSuccess() ? data.toString() : error.toString();
    }

    public final boolean isSuccess(){
        return error == null;
    }

    public final boolean isFailure(){
        return data == null;
    }

    public final T getData(){
        return data;
    }
    
    public final T getDataOrThrows() throws E{
        if(isSuccess()){
            return data;
        }
        throw error;
    }

    public final E getError(){
        return error;
    }

    public final String getErrorMessage(){
        return error.getMessage();
    }

    
    

}
