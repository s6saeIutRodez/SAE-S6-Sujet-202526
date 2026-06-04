package fr.adriencaubel.hotel.infra;

import fr.adriencaubel.hotel.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {


    @Query("""
    SELECT i
    FROM Inventory i
    WHERE i.roomType.id = :roomTypeId
      AND i.date >= :from
      AND i.date < :to
    ORDER BY i.date
    """)
    List<Inventory> findByRoomTypeAndDateBetween(
            @Param("roomTypeId") Long roomTypeId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    boolean existsByRoomTypeIdAndDate(Long roomTypeId, LocalDate date);

    Optional<Inventory> findByRoomTypeIdAndDate(Long roomTypeId, LocalDate date);
}
