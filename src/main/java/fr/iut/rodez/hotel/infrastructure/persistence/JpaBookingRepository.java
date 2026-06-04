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

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM BookingJpaEntity b WHERE b.status = :status")
    BigDecimal sumRevenueByStatus(@Param("status") String status);
}