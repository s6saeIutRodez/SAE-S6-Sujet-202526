package fr.iut.rodez.hotel.domain.port;

// Port secondaire (driven) — le domaine définit le contrat, l'infra l'implémente
public interface EmailPort {
    void sendBookingConfirmation(String toEmail, Long bookingId);
}