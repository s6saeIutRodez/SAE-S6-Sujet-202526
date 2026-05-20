package fr.iut.rodez.hotel.infrastructure.email;

import fr.iut.rodez.hotel.domain.port.out.EmailPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

// Driven adapter : implémente le port défini par le domaine
@Component
public class SmtpEmailAdapter implements EmailPort {

    private static final Logger log = LoggerFactory.getLogger(SmtpEmailAdapter.class);

    private final JavaMailSender mailSender;

    public SmtpEmailAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendBookingConfirmation(String toEmail, Long bookingId) {
        log.info("Envoi de confirmation à {} pour la réservation {}", toEmail, bookingId);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@hotel.com");
            message.setTo(toEmail);  // BUG CORRIGÉ : on utilise l'email du client
            message.setSubject("Confirmation de réservation #" + bookingId);
            message.setText("Votre réservation #" + bookingId + " est confirmée.");
            mailSender.send(message);
            log.info("Email envoyé avec succès à {}", toEmail);
        } catch (Exception e) {
            log.error("Échec de l'envoi de l'email à {} : {}", toEmail, e.getMessage());
        }
    }
}