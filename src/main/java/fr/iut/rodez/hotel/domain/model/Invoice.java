package fr.iut.rodez.hotel.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Agrégat facture — IMMUABLE après création.
 * Conformité anti-fraude TVA : aucun setter exposé,
 * updatable=false sur toutes les colonnes côté JPA.
 */
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
    private BigDecimal amount;        // figé au moment de l'émission
    private LocalDateTime issuedAt;

    protected Invoice() {}

    /** Seul point d'entrée métier — vérifie les invariants. */
    public static Invoice issue(Booking booking, String invoiceNumber) {
        if (booking == null)
            throw new IllegalArgumentException("La réservation est obligatoire");
        if (!BookingStatus.CONFIRMED.name().equals(booking.getStatus()))
            throw new IllegalStateException(
                    "Impossible d'émettre une facture pour une réservation non confirmée");
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
        inv.amount        = booking.getAmount();   // copie défensive
        inv.issuedAt      = LocalDateTime.now();
        return inv;
    }

    /** Reconstruction depuis la persistance uniquement — sans vérification des invariants métier. */
    public static Invoice reconstruct(Long id, String invoiceNumber, Long bookingId,
                                      String clientNom, String clientPrenom, String clientEmail,
                                      String roomTypeName, LocalDate fromDate, LocalDate toDate,
                                      int quantity, BigDecimal amount, LocalDateTime issuedAt) {
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
        inv.issuedAt      = issuedAt;
        return inv;
    }

    // Getters uniquement — pas de setters
    public Long getId()               { return id; }
    public String getInvoiceNumber()  { return invoiceNumber; }
    public Long getBookingId()        { return bookingId; }
    public String getClientNom()      { return clientNom; }
    public String getClientPrenom()   { return clientPrenom; }
    public String getClientEmail()    { return clientEmail; }
    public String getRoomTypeName()   { return roomTypeName; }
    public LocalDate getFromDate()    { return fromDate; }
    public LocalDate getToDate()      { return toDate; }
    public int getQuantity()          { return quantity; }
    public BigDecimal getAmount()     { return amount; }
    public LocalDateTime getIssuedAt(){ return issuedAt; }
}
