package com.project3.storemanagement.dto;

import com.project3.storemanagement.entities.Role;

import java.io.Serializable;
import java.util.Set;

public class LoginResponse implements Serializable {
    private String jwt;
    private Set<Role> role;
    private Long id;
    private String tokenType = "Bearer";

    public LoginResponse(String jwt, Set<Role> role, Long id) {
        this.jwt = jwt;
        this.role = role;
        this.id = id;
    }

    public Set<Role> getRole() {
        return role;
    }

    public String getJwt() {
        return jwt;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getId(){return  id;}
}

