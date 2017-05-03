package com.questizzle.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

/**
 * Created by Danny on 4/7/2017.
 */
@Component
public class MailClient {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String from, String to, String subject, String message) throws MailException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(message, true);
        };

        javaMailSender.send(messagePreparator);
    }

}
