package com.talhaunal.backend.controller.request;


public class OauthLoginRequest {

    private String idToken;

    public OauthLoginRequest() {
    }

    public OauthLoginRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
