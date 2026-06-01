package fr.iut.rodez.hotel.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository Spring Data JPA pour InventoryJpaEntity.
 * Les requêtes JPQL utilisent le nom de la classe JPA (InventoryJpaEntity).
 */
interface JpaInventoryRepository extends JpaRepository<InventoryJpaEntity, Long> {

    @Query("SELECT i FROM InventoryJpaEntity i WHERE i.roomType.id = :roomTypeId AND i.date = :date")
    Optional<InventoryJpaEntity> findByRoomTypeIdAndDate(
            @Param("roomTypeId") Long roomTypeId,
            @Param("date") LocalDate date);

    @Query("SELECT i FROM InventoryJpaEntity i WHERE i.roomType.id = :roomTypeId AND i.date BETWEEN :from AND :to")
    List<InventoryJpaEntity> findByRoomTypeIdAndDateBetween(
            @Param("roomTypeId") Long roomTypeId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    boolean existsByRoomTypeIdAndDate(Long roomTypeId, LocalDate date);
}