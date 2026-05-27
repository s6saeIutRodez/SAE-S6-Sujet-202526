package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.in.ICalculatePriceUseCase;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import fr.iut.rodez.hotel.domain.service.PricingDomainService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * US-06 — Calculer le prix d'un séjour
 * Délègue le calcul au PricingDomainService (logique métier pure, sans infra).
 */
@Service
public class CalculatePriceUseCase implements ICalculatePriceUseCase {

    private final RoomTypeRepository roomTypeRepository;
    private final PricingDomainService pricingDomainService;

    public CalculatePriceUseCase(RoomTypeRepository roomTypeRepository,
                                 PricingDomainService pricingDomainService) {
        this.roomTypeRepository = roomTypeRepository;
        this.pricingDomainService = pricingDomainService;
    }

    @Override
    public BigDecimal execute(Long roomTypeId, LocalDate from, LocalDate to, int quantity) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Type de chambre introuvable : " + roomTypeId));

        return pricingDomainService.calculateTotalPrice(roomType, from, to, quantity);
    }
}
