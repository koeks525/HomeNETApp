package com.koeksworld.homenet;

/**
 * Created by Okuhle on 2017/08/04.
 */

public class TokenSingleton {

    private String firebaseToken;
    private static TokenSingleton tokenSingleton;

    private TokenSingleton() {

    }

    public static TokenSingleton getTokenInstance() {
        if (tokenSingleton == null) {
            tokenSingleton = new TokenSingleton();
            return tokenSingleton;
        } else {
            return tokenSingleton;
        }
    }

    public void setToken(String token) {
        firebaseToken = token;
    }

    public String getToken() {
        return firebaseToken;
    }
}
