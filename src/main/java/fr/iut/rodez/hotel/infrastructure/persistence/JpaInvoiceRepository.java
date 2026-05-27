package fr.iut.rodez.hotel.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

interface JpaInvoiceRepository extends JpaRepository<InvoiceJpaEntity, Long> {
    Optional<InvoiceJpaEntity> findByInvoiceNumber(String invoiceNumber);
    List<InvoiceJpaEntity> findByBookingId(Long bookingId);
}
