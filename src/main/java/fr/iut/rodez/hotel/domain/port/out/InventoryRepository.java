package fr.iut.rodez.hotel.domain.port.out;

import fr.iut.rodez.hotel.domain.model.Inventory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository {
    Inventory save(Inventory inventory);
    Optional<Inventory> findByRoomTypeIdAndDate(Long roomTypeId, LocalDate date);
    List<Inventory> findByRoomTypeIdAndDateBetween(Long roomTypeId, LocalDate from, LocalDate to);
    boolean existsByRoomTypeIdAndDate(Long roomTypeId, LocalDate date);
}