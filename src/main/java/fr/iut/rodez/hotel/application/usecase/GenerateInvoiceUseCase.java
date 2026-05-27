package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.Booking;
import fr.iut.rodez.hotel.domain.model.Invoice;
import fr.iut.rodez.hotel.domain.port.in.IGenerateInvoiceUseCase;
import fr.iut.rodez.hotel.domain.port.out.BookingRepository;
import fr.iut.rodez.hotel.domain.port.out.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * US-08 — Émettre une facture immuable
 *
 * Conformité réglementaire anti-fraude TVA :
 *  - L'invariant (réservation CONFIRMED) est vérifié dans Invoice.issue()
 *  - Le montant est figé à l'instant de l'émission (copie défensive)
 *  - updatable=false sur toutes les colonnes JPA empêche tout UPDATE ultérieur
 */
@Service
public class GenerateInvoiceUseCase implements IGenerateInvoiceUseCase {

    private final BookingRepository bookingRepository;
    private final InvoiceRepository invoiceRepository;

    // En production : remplacer par une séquence DB dédiée pour garantir l'unicité
    private static final AtomicLong COUNTER = new AtomicLong(1);

    public GenerateInvoiceUseCase(BookingRepository bookingRepository,
                                  InvoiceRepository invoiceRepository) {
        this.bookingRepository = bookingRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    @Transactional
    public Invoice execute(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Réservation introuvable : " + bookingId));

        Invoice invoice = Invoice.issue(booking, generateNumber());
        return invoiceRepository.save(invoice);
    }

    private String generateNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return "FACT-" + date + "-" + String.format("%04d", COUNTER.getAndIncrement());
    }
}
