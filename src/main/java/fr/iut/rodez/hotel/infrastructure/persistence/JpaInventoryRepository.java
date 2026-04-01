package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

interface JpaInventoryRepository extends JpaRepository<Inventory, Long> {

    @Query("SELECT i FROM Inventory i WHERE i.roomType.id = :roomTypeId AND i.date = :date")
    Optional<Inventory> findByRoomTypeIdAndDate(@Param("roomTypeId") Long roomTypeId,
                                                @Param("date") LocalDate date);

    @Query("SELECT i FROM Inventory i WHERE i.roomType.id = :roomTypeId AND i.date BETWEEN :from AND :to")
    List<Inventory> findByRoomTypeIdAndDateBetween(@Param("roomTypeId") Long roomTypeId,
                                                   @Param("from") LocalDate from,
                                                   @Param("to") LocalDate to);

    boolean existsByRoomTypeIdAndDate(Long roomTypeId, LocalDate date);
}