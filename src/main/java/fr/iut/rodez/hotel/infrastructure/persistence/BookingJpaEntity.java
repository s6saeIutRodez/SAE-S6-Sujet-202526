package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.Booking;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA pour la table bookings.
 */
@Entity
@Table(name = "bookings")
public class BookingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomTypeJpaEntity roomType;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BookingOptionJpaEntity> options = new ArrayList<>();

    protected BookingJpaEntity() {}

    /**
     * Conversion domaine JPA.
     * Reçoit la référence JPA du room type pour éviter un SELECT supplémentaire.
     */
    static BookingJpaEntity fromDomain(Booking booking, RoomTypeJpaEntity roomTypeRef) {
        BookingJpaEntity e = new BookingJpaEntity();
        e.id       = booking.getId();
        e.roomType = roomTypeRef;
        e.fromDate = booking.getFromDate();
        e.toDate   = booking.getToDate();
        e.quantity = booking.getQuantity();
        e.amount   = booking.getAmount();
        e.status   = booking.getStatus();
        e.nom      = booking.getNom();
        e.prenom   = booking.getPrenom();
        e.email    = booking.getEmail();
        e.options  = booking.getOptions().stream()
                .map(o -> BookingOptionJpaEntity.fromDomain(o, e))
                .toList();
        return e;
    }

    /** Conversion JPA domaine avec reconstruction complète des options */
    Booking toDomain() {
        Booking b = Booking.reconstruct(
                id, roomType.toDomain(),
                fromDate, toDate,
                quantity, amount,
                status, nom, prenom, email
        );
        options.stream()
                .map(BookingOptionJpaEntity::toDomain)
                .forEach(b::addReconstructedOption);
        return b;
    }

    public Long getId() { return id; }
}
