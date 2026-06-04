package fr.iut.rodez.hotel.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;

/**
 * Repository Spring Data JPA pour BookingJpaEntity.
 *
 * ⚠️ IMPORTANT — COALESCE(SUM(decimal), 0) avec Hibernate 6.x :
 * Le littéral entier '0' provoque une incompatibilité de type avec BigDecimal.
 * Hibernate retourne alors Long/Double au lieu de BigDecimal, entraînant
 * une ClassCastException silencieuse dans le Gauge Micrometer (NaN → OTLP drop → 0 dans Grafana).
 *
 * Solution : utiliser SUM seul (retourne null si aucune ligne), puis gérer null dans l'adaptateur.
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