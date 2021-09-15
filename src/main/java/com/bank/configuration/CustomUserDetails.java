package com.bank.configuration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.math.BigInteger;
import java.util.Collection;

public class CustomUserDetails extends User {

    private BigInteger userId;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, BigInteger userId) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
