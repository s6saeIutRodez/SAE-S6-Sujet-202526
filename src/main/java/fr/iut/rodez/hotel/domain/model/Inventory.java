// domain/model/Inventory.java
package fr.iut.rodez.hotel.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inventories",
        uniqueConstraints = @UniqueConstraint(columnNames = {"room_type_id", "date"}))
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int totalRooms;

    @Column(nullable = false)
    private int reservedRooms;

    protected Inventory() {}

    public static Inventory create(RoomType roomType, LocalDate date, int totalRooms) {
        Inventory inv = new Inventory();
        inv.roomType = roomType;
        inv.date = date;
        inv.totalRooms = totalRooms;
        inv.reservedRooms = 0;
        return inv;
    }

    // Méthodes métier avec invariants protégés dans l'agrégat
    public void reserve(int quantity) {
        if (!canReserve(quantity)) {
            throw new IllegalStateException(
                    "Pas assez de chambres disponibles pour le " + date +
                            " (disponibles: " + availableRooms() + ", demandées: " + quantity + ")"
            );
        }
        this.reservedRooms += quantity;
    }

    public void release(int quantity) {
        if (quantity > reservedRooms) {
            throw new IllegalStateException("Impossible de libérer plus que le nombre réservé");
        }
        this.reservedRooms -= quantity;
    }

    public boolean canReserve(int quantity) {
        return availableRooms() >= quantity;
    }

    public int availableRooms() {
        return totalRooms - reservedRooms;
    }

    public Long getId() { return id; }
    public RoomType getRoomType() { return roomType; }
    public LocalDate getDate() { return date; }
    public int getTotalRooms() { return totalRooms; }
    public int getReservedRooms() { return reservedRooms; }
}