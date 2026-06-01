package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.Inventory;
import fr.iut.rodez.hotel.domain.port.out.InventoryRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Adaptateur : traduit entre Inventory (domaine) et InventoryJpaEntity (infra).
 *
 * Injecte JpaRoomTypeRepository pour obtenir une référence FK vers le room type
 * via getReferenceById() — évite un SELECT inutile tout en satisfaisant Hibernate.
 */
@Repository
public class InventoryRepositoryAdapter implements InventoryRepository {

    private final JpaInventoryRepository inventoryJpa;
    private final JpaRoomTypeRepository  roomTypeJpa;

    public InventoryRepositoryAdapter(JpaInventoryRepository inventoryJpa,
                                      JpaRoomTypeRepository roomTypeJpa) {
        this.inventoryJpa = inventoryJpa;
        this.roomTypeJpa  = roomTypeJpa;
    }

    @Override
    public Inventory save(Inventory inventory) {
        // getReferenceById : proxy FK sans SELECT supplémentaire sur room_types
        RoomTypeJpaEntity roomTypeRef = roomTypeJpa.getReferenceById(inventory.getRoomType().getId());
        InventoryJpaEntity entity = InventoryJpaEntity.fromDomain(inventory, roomTypeRef);
        return inventoryJpa.save(entity).toDomain();
    }

    @Override
    public Optional<Inventory> findByRoomTypeIdAndDate(Long id, LocalDate date) {
        return inventoryJpa.findByRoomTypeIdAndDate(id, date)
                .map(InventoryJpaEntity::toDomain);
    }

    @Override
    public List<Inventory> findByRoomTypeIdAndDateBetween(Long id, LocalDate from, LocalDate to) {
        return inventoryJpa.findByRoomTypeIdAndDateBetween(id, from, to).stream()
                .map(InventoryJpaEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsByRoomTypeIdAndDate(Long id, LocalDate date) {
        return inventoryJpa.existsByRoomTypeIdAndDate(id, date);
    }
}