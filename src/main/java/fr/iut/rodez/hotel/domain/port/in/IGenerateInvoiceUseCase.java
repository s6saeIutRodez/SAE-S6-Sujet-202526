package fr.iut.rodez.hotel.domain.port.in;

import fr.iut.rodez.hotel.domain.model.Invoice;

/**
 * US-08 — Émettre une facture immuable
 */
public interface IGenerateInvoiceUseCase {
    Invoice execute(Long bookingId);
}
