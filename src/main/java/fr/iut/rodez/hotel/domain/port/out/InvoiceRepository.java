package fr.iut.rodez.hotel.domain.port.out;

import fr.iut.rodez.hotel.domain.model.Invoice;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);
    Optional<Invoice> findById(Long id);
    Optional<Invoice> findByInvoiceNumber(String number);
    List<Invoice> findByBookingId(Long bookingId);
    List<Invoice> findAll();
}
