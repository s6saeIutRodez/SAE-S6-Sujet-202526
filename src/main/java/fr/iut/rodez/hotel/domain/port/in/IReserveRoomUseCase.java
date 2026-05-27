package fr.iut.rodez.hotel.domain.port.in;

import fr.iut.rodez.hotel.application.usecase.ReserveRoomUseCase;
import fr.iut.rodez.hotel.domain.model.Booking;

/**
 * US-04 — Créer une réservation
 * US-05 — Ajouter des options à une réservation
 */
public interface IReserveRoomUseCase {
    Booking execute(ReserveRoomUseCase.Command command);
}
