package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.Inventory;
import fr.iut.rodez.hotel.domain.port.out.InventoryRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class InventoryRepositoryAdapter implements InventoryRepository {

    private final JpaInventoryRepository jpa;

    public InventoryRepositoryAdapter(JpaInventoryRepository jpa) {
        this.jpa = jpa;
    }

    @Override public Inventory save(Inventory inventory) { return jpa.save(inventory); }
    @Override public Optional<Inventory> findByRoomTypeIdAndDate(Long id, LocalDate date) {
        return jpa.findByRoomTypeIdAndDate(id, date);
    }
    @Override public List<Inventory> findByRoomTypeIdAndDateBetween(Long id, LocalDate from, LocalDate to) {
        return jpa.findByRoomTypeIdAndDateBetween(id, from, to);
    }
    @Override public boolean existsByRoomTypeIdAndDate(Long id, LocalDate date) {
        return jpa.existsByRoomTypeIdAndDate(id, date);
    }
}