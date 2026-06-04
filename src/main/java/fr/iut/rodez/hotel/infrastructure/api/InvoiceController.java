package fr.iut.rodez.hotel.infrastructure.api;

import fr.iut.rodez.hotel.application.usecase.GenerateInvoiceUseCase;
import fr.iut.rodez.hotel.domain.port.out.InvoiceRepository;
import fr.iut.rodez.hotel.infrastructure.api.dto.InvoiceRequestDto;
import fr.iut.rodez.hotel.infrastructure.api.dto.InvoiceResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * US-08 — Émettre une facture immuable
 *
 * POST /invoices?bookingId={id}   → génère et persiste la facture
 * GET  /invoices/{id}             → consulte une facture par identifiant
 * GET  /invoices?bookingId={id}   → toutes les factures d'une réservation
 */
@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final GenerateInvoiceUseCase generateInvoiceUseCase;
    private final InvoiceRepository invoiceRepository;

    public InvoiceController(GenerateInvoiceUseCase generateInvoiceUseCase,
                             InvoiceRepository invoiceRepository) {
        this.generateInvoiceUseCase = generateInvoiceUseCase;
        this.invoiceRepository = invoiceRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponseDto generate(@RequestParam Long bookingId) {
        return InvoiceResponseDto.from(generateInvoiceUseCase.execute(bookingId));
    }

    @GetMapping("/{id}")
    public InvoiceResponseDto findById(@PathVariable Long id) {
        return invoiceRepository.findById(id)
                .map(InvoiceResponseDto::from)
                .orElseThrow(() -> new IllegalArgumentException("Facture introuvable : " + id));
    }

    @GetMapping
    public List<InvoiceResponseDto> findByBooking(@RequestParam Long bookingId) {
        return invoiceRepository.findByBookingId(bookingId).stream()
                .map(InvoiceResponseDto::from)
                .toList();
    }
}
