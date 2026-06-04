package fr.iut.rodez.hotel.domain.model;

import java.time.LocalDate;

public class Inventory {

    private Long id;
    private RoomType roomType;
    private LocalDate date;
    private int totalRooms;
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

    /**
     * Reconstruction depuis la persistance — pas de validation des invariants.
     * Utilisé uniquement par la couche infrastructure (InventoryJpaEntity.toDomain).
     */
    public static Inventory reconstruct(Long id, RoomType roomType, LocalDate date,
                                        int totalRooms, int reservedRooms) {
        Inventory inv = new Inventory();
        inv.id = id;
        inv.roomType = roomType;
        inv.date = date;
        inv.totalRooms = totalRooms;
        inv.reservedRooms = reservedRooms;
        return inv;
    }

    public void reserve(int quantity) {
        if (!canReserve(quantity))
            throw new IllegalStateException(
                    "Pas assez de chambres disponibles pour le " + date +
                            " (disponibles: " + availableRooms() + ", demandées: " + quantity + ")");
        this.reservedRooms += quantity;
    }

    public void release(int quantity) {
        if (quantity > reservedRooms)
            throw new IllegalStateException("Impossible de libérer plus que le nombre réservé");
        this.reservedRooms -= quantity;
    }

    public boolean canReserve(int quantity) { return availableRooms() >= quantity; }
    public int availableRooms()             { return totalRooms - reservedRooms; }

    public Long getId()           { return id; }
    public RoomType getRoomType() { return roomType; }
    public LocalDate getDate()    { return date; }
    public int getTotalRooms()    { return totalRooms; }
    public int getReservedRooms() { return reservedRooms; }
}