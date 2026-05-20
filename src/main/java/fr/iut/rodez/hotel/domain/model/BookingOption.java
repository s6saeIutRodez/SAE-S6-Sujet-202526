// domain/model/BookingOption.java
package fr.iut.rodez.hotel.domain.model;

import jakarta.persistence.*;

public class BookingOption {

    private Long id;

    private String type;

    private String comment;

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