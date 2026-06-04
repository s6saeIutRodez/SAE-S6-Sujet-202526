package fr.iut.rodez.hotel.domain.port.out;

public interface EmailPort {
    void sendBookingConfirmation(String toEmail, Long bookingId);
}
