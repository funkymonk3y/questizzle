package com.questizzle.security.models;

/**
 * Created by Danny on 3/22/2017.
 */
public class JwtAuthenticationResponse {

    private String token;

    public JwtAuthenticationResponse() {}

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
