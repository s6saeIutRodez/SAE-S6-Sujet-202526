package fr.iut.rodez.hotel.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

interface JpaInvoiceRepository extends JpaRepository<InvoiceJpaEntity, Long> {
    Optional<InvoiceJpaEntity> findByInvoiceNumber(String invoiceNumber);
    List<InvoiceJpaEntity> findByBookingId(Long bookingId);

    // Calcul de la somme TTC : HT * (1 + (taux_tva / 100))
    @Query("SELECT COALESCE(SUM(i.amount * (1 + (i.tvaRate / 100))), 0) FROM InvoiceJpaEntity i")
    BigDecimal sumTotalAmountTTC();
}