// domain/model/BookingOption.java
package fr.iut.rodez.hotel.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "booking_options")
public class BookingOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    protected BookingOption() {}

    // Constructeur package-private : seul Booking peut créer une option
    BookingOption(Booking booking, String type, String comment) {
        this.booking = booking;
        this.type = type;
        this.comment = comment;
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public String getComment() { return comment; }
}