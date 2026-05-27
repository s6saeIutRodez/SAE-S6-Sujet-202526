package fr.iut.rodez.hotel.domain.port.in;

import fr.iut.rodez.hotel.application.usecase.CreateRoomTypeUseCase;

/**
 * US-01 — Créer un type de chambre
 */
public interface ICreateRoomTypeUseCase {
    CreateRoomTypeUseCase.Result execute(String name, int totalRooms);
}
