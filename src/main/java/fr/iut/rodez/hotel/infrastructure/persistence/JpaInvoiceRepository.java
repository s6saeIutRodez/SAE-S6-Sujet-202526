package fr.iut.rodez.hotel.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Même correction que JpaBookingRepository :
 * SUM sans COALESCE pour éviter le type mismatch BigDecimal/Integer de Hibernate 6.x.
 */
interface JpaInvoiceRepository extends JpaRepository<InvoiceJpaEntity, Long> {

    Optional<InvoiceJpaEntity> findByInvoiceNumber(String invoiceNumber);
    List<InvoiceJpaEntity> findByBookingId(Long bookingId);

    /**
     * Calcul TTC : montant HT × (1 + tva/100).
     * Retourne null si la table est vide — géré par l'adaptateur.
     */
    @Query("SELECT SUM(i.amount * (1 + (i.tvaRate / 100))) FROM InvoiceJpaEntity i")
    BigDecimal sumTotalAmountTTC();
}