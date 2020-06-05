package com.example.demo_fcm;

import java.io.Serializable;

public class UserModel implements Serializable {
    public String email;
    public String token;

    public UserModel(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public UserModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
