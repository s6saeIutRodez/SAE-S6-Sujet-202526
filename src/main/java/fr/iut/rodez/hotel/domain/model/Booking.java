// domain/model/Booking.java
package fr.iut.rodez.hotel.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    @Column(nullable = false)
    private int quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingOption> options = new ArrayList<>();

    protected Booking() {}

    // Constructeur métier : invariants vérifiés à la création
    public static Booking create(RoomType roomType,
                                 LocalDate from, LocalDate to,
                                 int quantity, BigDecimal amount,
                                 String nom, String prenom, String email) {
        if (to.isBefore(from) || to.isEqual(from)) {
            throw new IllegalArgumentException("La date de départ doit être après la date d'arrivée");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant ne peut pas être négatif");
        }

        Booking b = new Booking();
        b.roomType = roomType;
        b.fromDate = from;
        b.toDate = to;
        b.quantity = quantity;
        b.amount = amount;
        b.status = BookingStatus.CONFIRMED.name();
        b.nom = nom;
        b.prenom = prenom;
        b.email = email;
        return b;
    }

    // Méthode métier : ajouter une option via la racine de l'agrégat
    public void addOption(String type, String comment) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Le type d'option ne peut pas être vide");
        }
        BookingOption option = new BookingOption(this, type, comment);
        this.options.add(option);
    }

    // Invariant : impossible d'annuler une réservation déjà annulée
    public void cancel() {
        if (BookingStatus.CANCELLED.name().equals(this.status)) {
            throw new IllegalStateException("La réservation est déjà annulée");
        }
        this.status = BookingStatus.CANCELLED.name();
    }

    public Long getId() { return id; }
    public RoomType getRoomType() { return roomType; }
    public LocalDate getFromDate() { return fromDate; }
    public LocalDate getToDate() { return toDate; }
    public int getQuantity() { return quantity; }
    public BigDecimal getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public List<BookingOption> getOptions() { return Collections.unmodifiableList(options); }
}