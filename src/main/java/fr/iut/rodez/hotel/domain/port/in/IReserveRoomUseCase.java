package fr.iut.rodez.hotel.domain.port.in;

import fr.iut.rodez.hotel.domain.model.Booking;
import java.time.LocalDate;
import java.util.List;

/**
 * US-04 — Créer une réservation
 * US-05 — Ajouter des options à une réservation
 */
public interface IReserveRoomUseCase {

    Booking execute(Command command);

    record Command(
            Long roomTypeId,
            LocalDate from,
            LocalDate to,
            int quantity,
            String nom,
            String prenom,
            String email,
            List<String> options
    ) {}
}