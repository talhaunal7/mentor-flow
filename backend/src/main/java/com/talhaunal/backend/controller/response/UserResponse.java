package com.talhaunal.backend.controller.response;

public class UserResponse {

    private String uid;
    private String role;
    private String name;
    private String surname;

    public UserResponse() {
    }

    public UserResponse(String uid, String role, String name, String surname) {
        this.uid = uid;
        this.role = role;
        this.name = name;
        this.surname = surname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
