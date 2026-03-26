package fr.adriencaubel.hotel.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    
    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);
    
    private final JavaMailSender mailSender;
    
    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendConfirmation(String to, String text) {
        log.info("Sending email to {}: {}", to, text);
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@hotel-legacy.com");
            message.setTo(to);
            message.setSubject("Hotel Booking Confirmation");
            message.setText(text);
            
            mailSender.send(message);
            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
