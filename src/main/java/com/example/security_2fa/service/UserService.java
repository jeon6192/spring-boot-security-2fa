package com.example.security_2fa.service;

import com.example.security_2fa.config.security.CustomUserDetails;
import com.example.security_2fa.mapper.UserMapper;
import com.example.security_2fa.model.dto.UserDto;
import com.example.security_2fa.model.entity.User;
import com.example.security_2fa.repository.UserRepository;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.IGoogleAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OTPService otpService;
    private final IGoogleAuthenticator googleAuthenticator;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, OTPService otpService, IGoogleAuthenticator googleAuthenticator) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.otpService = otpService;
        this.googleAuthenticator = googleAuthenticator;
    }

    public UserDto signup(UserDto userDto) throws NoSuchAlgorithmException {
        User user = UserMapper.INSTANCE.toEntity(userDto);

        user.encodePassword(passwordEncoder.encode(user.getPassword()));

        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        try {
            otpService.sendRegistrationMail(userDto, key);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new NoSuchAlgorithmException("FAILURE SEND OTP MAIL");
        }

        user.changeOtpSecretKey(key.getKey());

        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can Not Found User"));

        return CustomUserDetails.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .isLocked(user.getIsLocked())
                .authorities(user.getAuthorities())
                .otpKey(user.getOtpKey())
                .build();
    }
}
