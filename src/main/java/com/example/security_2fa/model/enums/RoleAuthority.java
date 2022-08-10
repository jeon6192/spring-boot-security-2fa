package com.example.security_2fa.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum RoleAuthority implements GrantedAuthority {
    ROLE_USER;

    @Override
    public String getAuthority() {
        return this.toString();
    }
}
