package com.example.security_2fa.config.security;

import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Getter
public class OTPWebAuthenticationDetails extends WebAuthenticationDetails {

    private static final String OTP_CODE = "otp-code";

    private Integer otpKey;

    public OTPWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        String otp = request.getParameter(OTP_CODE);

        if (StringUtils.hasText(otp)) {
            try {
                otpKey = Integer.valueOf(otp);
            } catch (NumberFormatException e) {
                otpKey = null;
            }
        }
    }
}
