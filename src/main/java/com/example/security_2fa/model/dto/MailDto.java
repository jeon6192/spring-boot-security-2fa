package com.example.security_2fa.model.dto;

import lombok.*;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MailDto {
    public static final String OTP_REGISTRATION = "otp-registration";

    private String from;

    private String replyTo;

    private String[] to;

    private String[] cc;

    private String[] bcc;

    private String subject;

    private String text;

    private boolean html;

    private List<File> attachments;

    private LocalDateTime sendDate;

    private String templateName;

    private Map<String, Object> attributes;

    @Builder
    public MailDto(String from, String replyTo, String[] to, String[] cc, String[] bcc, String subject, String text,
                   boolean html, List<File> attachments, LocalDateTime sendDate, String templateName, Map<String, Object> attributes) {
        this.from = from;
        this.replyTo = replyTo;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.text = text;
        this.html = html;
        this.attachments = attachments;
        this.sendDate = sendDate;
        this.templateName = templateName;
        this.attributes = attributes;
    }

    public boolean isMultipart() {
        return attachments == null || !attachments.isEmpty();
    }

    public boolean hasTemplateName() {
        return StringUtils.hasText(this.templateName);
    }

    public void useTemplate(String text) {
        this.text = text;
        this.html = true;
    }
}
