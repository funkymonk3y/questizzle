package com.questizzle.security.models;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Danny on 3/21/2017.
 */
public class SecRoleDetails implements GrantedAuthority {

    private String role;

    public SecRoleDetails(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
