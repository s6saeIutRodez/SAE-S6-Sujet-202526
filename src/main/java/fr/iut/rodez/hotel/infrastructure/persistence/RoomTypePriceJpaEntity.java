package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.RoomTypePrice;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entité JPA pour la table room_type_prices.
 */
@Entity
@Table(name = "room_type_prices")
public class RoomTypePriceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomTypeJpaEntity roomType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    protected RoomTypePriceJpaEntity() {}

    /** Conversion domaine → JPA. Reçoit l'entité parente déjà construite. */
    static RoomTypePriceJpaEntity fromDomain(RoomTypePrice p, RoomTypeJpaEntity roomTypeEntity) {
        RoomTypePriceJpaEntity e = new RoomTypePriceJpaEntity();
        e.id            = p.getId();
        e.roomType      = roomTypeEntity;
        e.startDate     = p.getStartDate();
        e.endDate       = p.getEndDate();
        e.pricePerNight = p.getPricePerNight();
        return e;
    }

    /** Conversion JPA → domaine */
    RoomTypePrice toDomain() {
        return RoomTypePrice.reconstruct(id, startDate, endDate, pricePerNight);
    }
}