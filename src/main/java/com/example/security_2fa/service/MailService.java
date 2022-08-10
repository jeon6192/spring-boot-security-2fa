package com.example.security_2fa.service;

import com.example.security_2fa.model.dto.MailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Slf4j
@Service
public class MailService {

    private final MailProperties mailProperties;
    private final SpringTemplateEngine springTemplateEngine;
    private final JavaMailSender javaMailSender;

    public MailService(MailProperties mailProperties, SpringTemplateEngine springTemplateEngine, JavaMailSender javaMailSender) {
        this.mailProperties = mailProperties;
        this.springTemplateEngine = springTemplateEngine;
        this.javaMailSender = javaMailSender;
    }

    public void send(MailDto mailDto) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, mailDto.isMultipart(), "UTF-8");
        messageHelper.setFrom(mailProperties.getUsername());
        messageHelper.setTo(mailDto.getTo());
        messageHelper.setSubject(mailDto.getSubject());

        if (mailDto.hasTemplateName()) {
            IContext context = new Context(LocaleContextHolder.getLocale(), mailDto.getAttributes());
            mailDto.useTemplate(springTemplateEngine.process(mailDto.getTemplateName(), context));
        }
        messageHelper.setText(mailDto.getText(), mailDto.isHtml());

        if (mailDto.getReplyTo() != null) messageHelper.setReplyTo(mailDto.getReplyTo());

        if (mailDto.getCc() != null) messageHelper.setCc(mailDto.getCc());

        if (mailDto.getBcc() != null) messageHelper.setBcc(mailDto.getBcc());

        if (mailDto.getAttachments() != null) {
            for (File attachment : mailDto.getAttachments()) {
                messageHelper.addAttachment(attachment.getName(), attachment);
            }
        }

        javaMailSender.send(mimeMessage);
    }
}
