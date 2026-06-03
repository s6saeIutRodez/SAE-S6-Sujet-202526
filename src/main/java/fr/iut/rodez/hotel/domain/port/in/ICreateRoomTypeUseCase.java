package fr.iut.rodez.hotel.domain.port.in;

/**
 * US-01 — Créer un type de chambre
 */
public interface ICreateRoomTypeUseCase {

    Result execute(Command command);

    record Command(String name, int totalRooms) {}

    record Result(Long id, String name, int totalRooms) {}
}