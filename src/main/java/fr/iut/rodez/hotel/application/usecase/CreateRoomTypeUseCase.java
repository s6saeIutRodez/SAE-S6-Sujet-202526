package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.in.ICreateRoomTypeUseCase;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * US-01 — Créer un type de chambre
 * Les invariants (nom non vide, totalRooms > 0) sont vérifiés dans RoomType.create().
 */
@Service
public class CreateRoomTypeUseCase implements ICreateRoomTypeUseCase {

    private final RoomTypeRepository roomTypeRepository;

    public CreateRoomTypeUseCase(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    @Transactional
    public Result execute(String name, int totalRooms) {
        RoomType roomType = RoomType.create(name, totalRooms);
        RoomType saved = roomTypeRepository.save(roomType);
        return new Result(saved.getId(), saved.getName(), saved.getTotalRooms());
    }

    public record Result(Long id, String name, int totalRooms) {}
}
