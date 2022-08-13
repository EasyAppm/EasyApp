package com.easyapp.net.http.entity;

import com.easyapp.util.SimpleBase64;

public abstract class Auth {

    protected String auth;

    protected Auth(String auth){
        this.auth = auth;
    }

    public String get(){
        return auth;
    }

    public static class Basic extends Auth{
        private String username, password;

        public Basic(String username, String password){
            super("Basic " + encode(username, password));
            this.username = username;
            this.password = password;
        }

        public String getUsername(){
            return username;
        }

        public String getPassword(){
            return password;
        }

        private static String encode(String username, String password){
            //Flag NO_WRAP evita quebra de linha, ilegal no header
            return SimpleBase64.encodeToString((username+":"+password), SimpleBase64.NO_WRAP);
        }
    }

    public static class Token extends Auth{
        public Token(String token){
            super(token);
        }
    }

    public static class BearerToken extends Auth{
        private static final String BEARER = "Bearer"; 
        public BearerToken(String token){
            super(token.contains(BEARER) ? token : BEARER + " " + token); 
        }
    }  
}
