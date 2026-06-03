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

@Service
public class GenerateInvoiceUseCase implements IGenerateInvoiceUseCase {

    private final BookingRepository bookingRepository;
    private final InvoiceRepository invoiceRepository;
    private static final AtomicLong COUNTER = new AtomicLong(1);

    public GenerateInvoiceUseCase(BookingRepository bookingRepository,
                                  InvoiceRepository invoiceRepository) {
        this.bookingRepository = bookingRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    @Transactional
    public Invoice execute(Long bookingId) {
        // 1. Validation Cas Limite : Empêcher les doublons de facture
        if (!invoiceRepository.findByBookingId(bookingId).isEmpty()) {
            throw new IllegalStateException(
                    "Une facture a déjà été émise pour la réservation ID : " + bookingId
            );
        }

        // 2. Récupération du Booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Réservation introuvable : " + bookingId));

        // 3. Appel au Domaine avec tes 2 arguments
        Invoice invoice = Invoice.issue(booking, generateNumber());
        return invoiceRepository.save(invoice);
    }

    private String generateNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return "FACT-" + date + "-" + String.format("%04d", COUNTER.getAndIncrement());
    }
}