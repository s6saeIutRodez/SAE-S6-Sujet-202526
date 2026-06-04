package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.in.ICreateRoomTypeUseCase;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateRoomTypeUseCase implements ICreateRoomTypeUseCase {

    private final RoomTypeRepository roomTypeRepository;

    public CreateRoomTypeUseCase(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    @Transactional
    public Result execute(Command command) {
        RoomType roomType = RoomType.create(command.name(), command.totalRooms());
        RoomType saved    = roomTypeRepository.save(roomType);
        return new Result(saved.getId(), saved.getName(), saved.getTotalRooms());
    }
}