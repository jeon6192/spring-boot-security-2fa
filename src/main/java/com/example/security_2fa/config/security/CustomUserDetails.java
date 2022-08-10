package com.example.security_2fa.config.security;

import com.example.security_2fa.model.enums.RoleAuthority;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@Getter
public class CustomUserDetails implements OTPUserDetails {

    private String username;

    private String password;

    private Boolean isLocked;

    private Set<RoleAuthority> authorities;

    private String otpKey;

    @Builder
    public CustomUserDetails(String username, String password, Boolean isLocked, Set<RoleAuthority> authorities, String otpKey) {
        this.username = username;
        this.password = password;
        this.isLocked = isLocked;
        this.authorities = authorities;
        this.otpKey = otpKey;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
