package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.BookingOption;
import jakarta.persistence.*;

/**
 * Entité JPA pour la table booking_options.
 */
@Entity
@Table(name = "booking_options")
public class BookingOptionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private BookingJpaEntity booking;

    @Column(nullable = false)
    private String type;

    @Column
    private String comment;

    protected BookingOptionJpaEntity() {}

    /** Conversion domaine → JPA. Reçoit l'entité parente déjà construite. */
    static BookingOptionJpaEntity fromDomain(BookingOption option, BookingJpaEntity bookingEntity) {
        BookingOptionJpaEntity e = new BookingOptionJpaEntity();
        e.id      = option.getId();
        e.booking = bookingEntity;
        e.type    = option.getType();
        e.comment = option.getComment();
        return e;
    }

    /** Conversion JPA → domaine avec préservation de l'ID */
    BookingOption toDomain() {
        return BookingOption.reconstruct(id, type, comment);
    }

    public Long getId()      { return id; }
    public String getType()  { return type; }
    public String getComment(){ return comment; }
}