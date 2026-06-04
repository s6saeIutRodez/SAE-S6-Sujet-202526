package fr.iut.rodez.hotel.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Invoice {

    private Long id;
    private String invoiceNumber;
    private Long bookingId;
    private String clientNom;
    private String clientPrenom;
    private String clientEmail;
    private String roomTypeName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int quantity;
    private BigDecimal amount;
    private BigDecimal tvaRate; // Ajouté pour la conformité TVA
    private LocalDateTime issuedAt;

    protected Invoice() {}

    /** Seul point d'entrée métier — Vérification stricte des invariants */
    public static Invoice issue(Booking booking, String invoiceNumber) {
        if (booking == null)
            throw new IllegalArgumentException("La réservation est obligatoire");

        // Validation Cas d'erreur : Seul le statut CONFIRMED permet de facturer
        if (!"CONFIRMED".equals(booking.getStatus())) {
            throw new IllegalStateException(
                    "Impossible d'émettre une facture pour une réservation avec le statut : " + booking.getStatus()
            );
        }

        if (invoiceNumber == null || invoiceNumber.isBlank())
            throw new IllegalArgumentException("Le numéro de facture est obligatoire");

        Invoice inv = new Invoice();
        inv.invoiceNumber = invoiceNumber;
        inv.bookingId     = booking.getId();
        inv.clientNom     = booking.getNom();
        inv.clientPrenom  = booking.getPrenom();
        inv.clientEmail   = booking.getEmail();
        inv.roomTypeName  = booking.getRoomType().getName();
        inv.fromDate      = booking.getFromDate();
        inv.toDate        = booking.getToDate();
        inv.quantity      = booking.getQuantity();
        inv.amount        = booking.getAmount();
        inv.tvaRate       = new BigDecimal("10.00"); // Taux fixe de l'hôtel (ex: 10%)
        inv.issuedAt      = LocalDateTime.now();
        return inv;
    }

    public static Invoice reconstruct(Long id, String invoiceNumber, Long bookingId,
                                      String clientNom, String clientPrenom, String clientEmail,
                                      String roomTypeName, LocalDate fromDate, LocalDate toDate,
                                      int quantity, BigDecimal amount, BigDecimal tvaRate, LocalDateTime issuedAt) {
        Invoice inv = new Invoice();
        inv.id            = id;
        inv.invoiceNumber = invoiceNumber;
        inv.bookingId     = bookingId;
        inv.clientNom     = clientNom;
        inv.clientPrenom  = clientPrenom;
        inv.clientEmail   = clientEmail;
        inv.roomTypeName  = roomTypeName;
        inv.fromDate      = fromDate;
        inv.toDate        = toDate;
        inv.quantity      = quantity;
        inv.amount        = amount;
        inv.tvaRate       = tvaRate;
        inv.issuedAt      = issuedAt;
        return inv;
    }

    // Getters uniquement (Pas de setters pour garantir l'inaltérabilité)
    public Long getId()                { return id; }
    public String getInvoiceNumber()   { return invoiceNumber; }
    public Long getBookingId()         { return bookingId; }
    public String getClientNom()       { return clientNom; }
    public String getClientPrenom()    { return clientPrenom; }
    public String getClientEmail()     { return clientEmail; }
    public String getRoomTypeName()    { return roomTypeName; }
    public LocalDate getFromDate()     { return fromDate; }
    public LocalDate getToDate()       { return toDate; }
    public int getQuantity()           { return quantity; }
    public BigDecimal getAmount()      { return amount; }
    public BigDecimal getTvaRate()     { return tvaRate; }
    public LocalDateTime getIssuedAt() { return issuedAt; }

    public BigDecimal getAmountTTC() {
        if (this.amount == null) {
            return BigDecimal.ZERO;
        }
        if (this.tvaRate == null) {
            return this.amount;
        }
        // Calcul : HT * (1 + TVA/100)
        BigDecimal tvaMultiplier = BigDecimal.ONE.add(this.tvaRate.divide(new BigDecimal("100")));
        return this.amount.multiply(tvaMultiplier);
    }
}