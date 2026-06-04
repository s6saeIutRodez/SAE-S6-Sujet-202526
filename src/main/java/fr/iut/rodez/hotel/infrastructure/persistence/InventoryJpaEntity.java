package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.Inventory;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entité JPA pour la table inventories.
 */
@Entity
@Table(name = "inventories")
public class InventoryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomTypeJpaEntity roomType;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "total_rooms", nullable = false)
    private int totalRooms;

    @Column(name = "reserved_rooms", nullable = false)
    private int reservedRooms;

    protected InventoryJpaEntity() {}

    /**
     * Conversion domaine  JPA.
     * Reçoit la référence JPA du room type pour éviter un SELECT supplémentaire.
     */
    static InventoryJpaEntity fromDomain(Inventory inventory, RoomTypeJpaEntity roomTypeRef) {
        InventoryJpaEntity e = new InventoryJpaEntity();
        e.id            = inventory.getId();
        e.roomType      = roomTypeRef;
        e.date          = inventory.getDate();
        e.totalRooms    = inventory.getTotalRooms();
        e.reservedRooms = inventory.getReservedRooms();
        return e;
    }

    /** Conversion JPA  domaine */
    Inventory toDomain() {
        return Inventory.reconstruct(id, roomType.toDomain(), date, totalRooms, reservedRooms);
    }

    public Long getId()           { return id; }
    public LocalDate getDate()    { return date; }
    public int getTotalRooms()    { return totalRooms; }
    public int getReservedRooms() { return reservedRooms; }
}
