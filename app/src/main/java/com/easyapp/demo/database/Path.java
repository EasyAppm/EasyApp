package com.easyapp.demo.database;

public class Path {
    
    private final String name;

    public Path(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return name;
    }
   
    
}
