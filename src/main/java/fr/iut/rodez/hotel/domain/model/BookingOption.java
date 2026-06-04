package fr.iut.rodez.hotel.domain.model;

public class BookingOption {

    private Long id;
    private String type;
    private String comment;
    private Booking booking;

    protected BookingOption() {}

    BookingOption(Booking booking, String type, String comment) {
        this.booking = booking;
        this.type = type;
        this.comment = comment;
    }

    /**
     * Reconstruction depuis la persistance — conserve l'identifiant existant.
     * Utilisé uniquement par la couche infrastructure (BookingOptionJpaEntity.toDomain).
     */
    public static BookingOption reconstruct(Long id, String type, String comment) {
        BookingOption o = new BookingOption();
        o.id = id;
        o.type = type;
        o.comment = comment;
        return o;
    }

    public Long getId()      { return id; }
    public String getType()  { return type; }
    public String getComment(){ return comment; }
}
