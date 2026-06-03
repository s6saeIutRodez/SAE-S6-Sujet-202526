package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.in.ICalculatePriceUseCase;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import fr.iut.rodez.hotel.domain.service.PricingDomainService;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;

@Service
public class CalculatePriceUseCase implements ICalculatePriceUseCase {

    private final RoomTypeRepository roomTypeRepository;
    private final PricingDomainService pricingDomainService;

    public CalculatePriceUseCase(RoomTypeRepository roomTypeRepository,
                                 PricingDomainService pricingDomainService) {
        this.roomTypeRepository   = roomTypeRepository;
        this.pricingDomainService = pricingDomainService;
    }

    @Override
    public Result execute(Command command) {
        RoomType roomType = roomTypeRepository.findById(command.roomTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Type de chambre introuvable : " + command.roomTypeId()));

        var total  = pricingDomainService.calculateTotalPrice(
                roomType, command.from(), command.to(), command.quantity());
        int nights = (int) ChronoUnit.DAYS.between(command.from(), command.to());

        return new Result(total, nights, command.quantity());
    }
}