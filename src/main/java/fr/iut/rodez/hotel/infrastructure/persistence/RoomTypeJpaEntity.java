package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.RoomType;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA pour la table room_types.
 * Séparée du modèle domaine RoomType pour respecter l'architecture hexagonale :
 * les annotations JPA n'ont pas leur place dans le domaine métier.
 */
@Entity
@Table(name = "room_types")
public class RoomTypeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "total_rooms", nullable = false)
    private int totalRooms;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RoomTypePriceJpaEntity> prices = new ArrayList<>();

    protected RoomTypeJpaEntity() {}

    /**
     * Crée une référence légère (proxy FK) sans charger l'entité depuis la base.
     * Utilisé par BookingJpaEntity et InventoryJpaEntity pour setter la FK sans déclencher de SELECT.
     */
    static RoomTypeJpaEntity reference(Long id) {
        RoomTypeJpaEntity e = new RoomTypeJpaEntity();
        e.id = id;
        return e;
    }

    /** Conversion domaine  JPA */
    static RoomTypeJpaEntity fromDomain(RoomType rt) {
        RoomTypeJpaEntity e = new RoomTypeJpaEntity();
        e.id         = rt.getId();
        e.name       = rt.getName();
        e.totalRooms = rt.getTotalRooms();
        e.prices = rt.getPrices().stream()
                .map(p -> RoomTypePriceJpaEntity.fromDomain(p, e))
                .toList();
        return e;
    }

    /** Conversion JPA  domaine */
    RoomType toDomain() {
        RoomType rt = RoomType.reconstruct(id, name, totalRooms);
        prices.stream()
                .map(RoomTypePriceJpaEntity::toDomain)
                .forEach(rt::addPrice);
        return rt;
    }

    public Long getId()                             { return id; }
    public String getName()                         { return name; }
    public int getTotalRooms()                      { return totalRooms; }
    public List<RoomTypePriceJpaEntity> getPrices() { return prices; }
}
