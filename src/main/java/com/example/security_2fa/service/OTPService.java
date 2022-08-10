package com.example.security_2fa.service;

import com.example.security_2fa.model.dto.MailDto;
import com.example.security_2fa.model.dto.UserDto;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OTPService {

    private static final String ISSUER = "sw";

    private final MailService mailService;

    public OTPService(MailService mailService) {
        this.mailService = mailService;
    }

    public void sendRegistrationMail(UserDto userDto, GoogleAuthenticatorKey key) throws MessagingException {
        String qrCodeUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL(ISSUER, userDto.getEmail(), key);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("codeUrl", qrCodeUrl);

        MailDto mailDto = MailDto.builder()
                .templateName("mails/" + MailDto.OTP_REGISTRATION)
                .to(new String[] { userDto.getEmail() })
                .subject("OTP Registration Mail")
                .attributes(attributes)
                .build();

        mailService.send(mailDto);
    }
}
