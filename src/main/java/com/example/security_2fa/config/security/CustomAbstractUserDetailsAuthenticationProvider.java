package com.example.security_2fa.config.security;

import com.warrenstrange.googleauth.IGoogleAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Slf4j
public class CustomAbstractUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final IGoogleAuthenticator googleAuthenticator;

    public CustomAbstractUserDetailsAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService,
                                                           IGoogleAuthenticator googleAuthenticator) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.googleAuthenticator = googleAuthenticator;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        if (authentication.getCredentials() == null) throw new BadCredentialsException("No Credentials");

        String credentialsPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(credentialsPassword, userDetails.getPassword()))
            throw new BadCredentialsException("BadCredentials");
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        OTPUserDetails userDetails;

        try {
            userDetails = (OTPUserDetails) userDetailsService.loadUserByUsername(username);

            if (userDetails == null) throw new InternalAuthenticationServiceException("UserDetails Is Null");

            if (!StringUtils.hasText(userDetails.getOtpKey()))
                throw new BadCredentialsException("Doesn't Registry OTP");

            Integer code = ((OTPWebAuthenticationDetails) authentication.getDetails()).getOtpKey();
            if (code != null) {
                if (!googleAuthenticator.authorize(userDetails.getOtpKey(), code)) {
                    throw new BadCredentialsException("Invalid Code");
                }
            } else {
                throw new BadCredentialsException("OTP Is Null");
            }
        } catch (UsernameNotFoundException e) {
            if (hideUserNotFoundExceptions) throw new BadCredentialsException("Bad Credentials");

            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }

        return userDetails;
    }
}
