package com.talhaunal.backend.controller.response;


public class LoginResponse {

    private String idToken;

    public LoginResponse(String idToken) {
        this.idToken = idToken;
    }

    public LoginResponse() {
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
