package com.example.security_2fa.config.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface OTPUserDetails extends UserDetails {
    String getOtpKey();
}
