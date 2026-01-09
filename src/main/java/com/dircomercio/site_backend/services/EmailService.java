package com.dircomercio.site_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@Profile("!docker")
public class EmailService {
    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void enviarEmail(String destinatario, String asunto, String mensaje) throws MessagingException {
        //SimpleMailMessage email = new SimpleMailMessage();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(mensaje, true);
        mailSender.send(message);
    }
}
