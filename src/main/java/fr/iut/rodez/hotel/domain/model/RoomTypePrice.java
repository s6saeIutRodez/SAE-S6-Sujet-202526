package fr.iut.rodez.hotel.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RoomTypePrice {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal pricePerNight;

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