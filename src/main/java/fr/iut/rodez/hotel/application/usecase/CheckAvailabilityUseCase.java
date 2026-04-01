package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.RoomTypeRepository;
import fr.iut.rodez.hotel.domain.service.AvailabilityDomainService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class CheckAvailabilityUseCase {

    private final RoomTypeRepository roomTypeRepository;
    private final AvailabilityDomainService availabilityService;

    public CheckAvailabilityUseCase(RoomTypeRepository roomTypeRepository,
                                    AvailabilityDomainService availabilityService) {
        this.roomTypeRepository = roomTypeRepository;
        this.availabilityService = availabilityService;
    }

    public Result execute(Long roomTypeId, LocalDate from, LocalDate to, int qty) {
        int quantity = qty <= 0 ? 1 : qty;

        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Type de chambre introuvable : " + roomTypeId));

        int remaining = availabilityService.getMinAvailableRooms(roomType, from, to);
        boolean available = remaining >= quantity;

        return new Result(roomTypeId, from, to, available, remaining);
    }

    public record Result(
            Long roomTypeId,
            LocalDate from,
            LocalDate to,
            boolean available,
            int remainingRooms
    ) {}
}