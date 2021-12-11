package nl.hsleiden.AfkoAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Sends an email.
 * @author Daniel Paans
 */
@Service
public class EmailSenderService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender MAIL_SENDER;

    @Autowired
    public EmailSenderService(JavaMailSender mailSender) {
        this.MAIL_SENDER = mailSender;
    }

    /**
     * Sends email.
     * @param toEmail
     * @param subject
     * @param body
     */
    public void sendEmail(String toEmail, String subject, String body){
        SimpleMailMessage message= new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        MAIL_SENDER.send(message);

        System.out.println("Mail Sent succesfully...");
    }

}
