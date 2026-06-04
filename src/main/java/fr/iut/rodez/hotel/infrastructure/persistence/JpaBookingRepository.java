package fr.iut.rodez.hotel.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;

/**
 * Repository Spring Data JPA pour BookingJpaEntity.
 */
interface JpaBookingRepository extends JpaRepository<BookingJpaEntity, Long> {

    long countByStatus(String status);

    /**
     * Retourne null si aucune réservation ne correspond (jamais de ClassCastException).
     * L'adaptateur ou le Gauge lambda gère le cas null avec BigDecimal.ZERO.
     */
    @Query("SELECT SUM(b.amount) FROM BookingJpaEntity b WHERE b.status = :status")
    BigDecimal sumRevenueByStatus(@Param("status") String status);
}
