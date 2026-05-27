package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.in.IGetRoomTypeUseCase;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * US-01 — Consulter la liste des types de chambres et leurs tarifs
 * Projette RoomType en Result (DTO interne) pour ne pas exposer les entités domaine.
 */
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
        List<PriceResult> prices = rt.getPrices().stream()
                .map(p -> new PriceResult(p.getStartDate(), p.getEndDate(), p.getPricePerNight()))
                .toList();
        return new Result(rt.getId(), rt.getName(), rt.getTotalRooms(), prices);
    }

    public record Result(Long id, String name, int totalRooms, List<PriceResult> prices) {}

    public record PriceResult(LocalDate startDate, LocalDate endDate, BigDecimal pricePerNight) {}
}
