package fr.iut.rodez.hotel.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "room_type_prices")
public class RoomTypePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    protected RoomTypePrice() {}

    public static RoomTypePrice create(LocalDate start, LocalDate end, BigDecimal price) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le prix doit être positif");
        }
        RoomTypePrice p = new RoomTypePrice();
        p.startDate = start;
        p.endDate = end;
        p.pricePerNight = price;
        return p;
    }

    // Appelé uniquement par RoomType.addPrice — information hiding
    void assignTo(RoomType roomType) {
        this.roomType = roomType;
    }

    public boolean covers(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public Long getId() { return id; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public BigDecimal getPricePerNight() { return pricePerNight; }
}