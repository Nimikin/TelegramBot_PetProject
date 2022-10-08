package com.nimikin.TelegramBot.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Setter
public class EmailSenderService {
    public String link;
    public String size;
    public String color;
    public String additionalInfo;
    private final JavaMailSender mailSender;
    @Value("${mail.username}")
    private String username;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail, String subject, String body) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(username);
            messageHelper.setTo(toEmail);
            messageHelper.setText(body);
            messageHelper.setSubject(subject);
        };
        this.mailSender.send(preparator);

        log.info("Message has been sent successfully");
    }

    public String messageToMail() {
        return this.link + "\n" +
                this.size + "\n" +
                this.color + "\n" +
                this.additionalInfo;
    }
}
