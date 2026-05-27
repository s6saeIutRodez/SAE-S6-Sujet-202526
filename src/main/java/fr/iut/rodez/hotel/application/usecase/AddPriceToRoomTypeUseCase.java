package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.model.RoomTypePrice;
import fr.iut.rodez.hotel.domain.port.in.IAddPriceToRoomTypeUseCase;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * US-02 — Définir un tarif par période
 * Associe une période tarifaire à un type de chambre existant.
 * Les invariants (dates cohérentes, prix > 0) sont vérifiés dans RoomTypePrice.create().
 */
@Service
public class AddPriceToRoomTypeUseCase implements IAddPriceToRoomTypeUseCase {

    private final RoomTypeRepository roomTypeRepository;

    public AddPriceToRoomTypeUseCase(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    @Transactional
    public void execute(Long roomTypeId, LocalDate startDate, LocalDate endDate, BigDecimal pricePerNight) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Type de chambre introuvable : " + roomTypeId));

        // Création du prix — invariants vérifiés dans la fabrique statique
        RoomTypePrice price = RoomTypePrice.create(startDate, endDate, pricePerNight);

        // Ajout via la racine d'agrégat → information hiding respecté
        roomType.addPrice(price);

        roomTypeRepository.save(roomType);
    }
}
