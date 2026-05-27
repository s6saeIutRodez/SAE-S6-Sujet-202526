package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.Invoice;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entité JPA pour la table invoices.
 * La table est créée automatiquement par Hibernate via spring.jpa.hibernate.ddl-auto=update|create.
 *
 * Immuabilité garantie à deux niveaux :
 *  1. Domaine : Invoice n'a aucun setter
 *  2. JPA     : updatable=false sur toutes les colonnes métier → Hibernate ne génère jamais d'UPDATE
 */
@Entity
@Table(name = "invoices")
public class InvoiceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", nullable = false, unique = true, updatable = false)
    private String invoiceNumber;

    @Column(name = "booking_id", nullable = false, updatable = false)
    private Long bookingId;

    @Column(name = "client_nom", nullable = false, updatable = false)
    private String clientNom;

    @Column(name = "client_prenom", nullable = false, updatable = false)
    private String clientPrenom;

    @Column(name = "client_email", nullable = false, updatable = false)
    private String clientEmail;

    @Column(name = "room_type_name", nullable = false, updatable = false)
    private String roomTypeName;

    @Column(name = "from_date", nullable = false, updatable = false)
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false, updatable = false)
    private LocalDate toDate;

    @Column(nullable = false, updatable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2, updatable = false)
    private BigDecimal amount;

    @Column(name = "issued_at", nullable = false, updatable = false)
    private LocalDateTime issuedAt;

    protected InvoiceJpaEntity() {}

    public static InvoiceJpaEntity fromDomain(Invoice inv) {
        InvoiceJpaEntity e = new InvoiceJpaEntity();
        e.id            = inv.getId();
        e.invoiceNumber = inv.getInvoiceNumber();
        e.bookingId     = inv.getBookingId();
        e.clientNom     = inv.getClientNom();
        e.clientPrenom  = inv.getClientPrenom();
        e.clientEmail   = inv.getClientEmail();
        e.roomTypeName  = inv.getRoomTypeName();
        e.fromDate      = inv.getFromDate();
        e.toDate        = inv.getToDate();
        e.quantity      = inv.getQuantity();
        e.amount        = inv.getAmount();
        e.issuedAt      = inv.getIssuedAt();
        return e;
    }

    public Invoice toDomain() {
        return Invoice.reconstruct(
                id, invoiceNumber, bookingId,
                clientNom, clientPrenom, clientEmail,
                roomTypeName, fromDate, toDate,
                quantity, amount, issuedAt
        );
    }
}
