package com.questizzle.security.models;

import com.questizzle.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Danny on 3/21/2017.
 */
public class SecUserDetails implements UserDetails {

    private User user;

    public SecUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SecRoleDetails> roleDetails = new ArrayList<>();

        for(String role : user.getRoles()) {
            roleDetails.add(new SecRoleDetails(role));
        }

        return roleDetails;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getAccountEnabled();
    }

    public Date getLastPasswordResetDate() {
        return user.getLastPasswordResetDate();
    }
}
