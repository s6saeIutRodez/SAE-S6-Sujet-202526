package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.in.IGetRoomTypeUseCase;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GetRoomTypeUseCase implements IGetRoomTypeUseCase {

    private final RoomTypeRepository roomTypeRepository;

    public GetRoomTypeUseCase(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public Result execute(Long id) {
        RoomType rt = roomTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Type de chambre introuvable : " + id));
        return toResult(rt);
    }

    @Override
    public List<Result> executeAll() {
        return roomTypeRepository.findAll().stream()
                .map(this::toResult)
                .toList();
    }

    private Result toResult(RoomType rt) {
        var prices = rt.getPrices().stream()
                .map(p -> new PriceResult(p.getStartDate(), p.getEndDate(), p.getPricePerNight()))
                .toList();
        return new Result(rt.getId(), rt.getName(), rt.getTotalRooms(), prices);
    }
}