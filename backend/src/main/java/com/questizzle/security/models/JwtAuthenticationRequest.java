package com.questizzle.security.models;

/**
 * Created by Danny on 3/22/2017.
 */
public class JwtAuthenticationRequest {

    private String username;
    private String password;

    public JwtAuthenticationRequest() {}

    public JwtAuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
