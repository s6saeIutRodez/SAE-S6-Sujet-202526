package fr.iut.rodez.hotel.domain.port.in;

/**
 * US-09 — Annuler une réservation
 */
public interface ICancelBookingUseCase {
    void execute(Long bookingId);
}
