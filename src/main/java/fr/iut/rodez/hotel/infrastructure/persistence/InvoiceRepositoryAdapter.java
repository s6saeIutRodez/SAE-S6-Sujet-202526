package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.Invoice;
import fr.iut.rodez.hotel.domain.port.out.InvoiceRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class InvoiceRepositoryAdapter implements InvoiceRepository {

    private final JpaInvoiceRepository jpa;

    public InvoiceRepositoryAdapter(JpaInvoiceRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Invoice save(Invoice invoice) {
        return jpa.save(InvoiceJpaEntity.fromDomain(invoice)).toDomain();
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return jpa.findById(id).map(InvoiceJpaEntity::toDomain);
    }

    @Override
    public Optional<Invoice> findByInvoiceNumber(String number) {
        return jpa.findByInvoiceNumber(number).map(InvoiceJpaEntity::toDomain);
    }

    @Override
    public List<Invoice> findByBookingId(Long bookingId) {
        return jpa.findByBookingId(bookingId).stream()
                .map(InvoiceJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Invoice> findAll() {
        return jpa.findAll().stream()
                .map(InvoiceJpaEntity::toDomain)
                .toList();
    }

    @Override
    public long countAll() {
        return jpa.count();
    }

    /**
     * SUM retourne null si la table est vide — on renvoie BigDecimal.ZERO pour éviter
     * un NullPointerException dans le Gauge Micrometer (émettrait NaN → supprimé par OTLP).
     */
    @Override
    public BigDecimal sumTotalAmountTTC() {
        BigDecimal result = jpa.sumTotalAmountTTC();
        return result != null ? result : BigDecimal.ZERO;
    }
}