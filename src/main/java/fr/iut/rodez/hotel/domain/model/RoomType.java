package fr.iut.rodez.hotel.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomType {

    private Long id;
    private String name;
    private int totalRooms;
    private List<RoomTypePrice> prices = new ArrayList<>();

    protected RoomType() {}

    public static RoomType create(String name, int totalRooms) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Le nom du type de chambre est obligatoire");
        if (totalRooms <= 0)
            throw new IllegalArgumentException("Le nombre de chambres doit être positif");
        RoomType rt = new RoomType();
        rt.name = name;
        rt.totalRooms = totalRooms;
        return rt;
    }

    /**
     * Reconstruction depuis la persistance — pas de validation des invariants.
     * Utilisé uniquement par la couche infrastructure (RoomTypeJpaEntity.toDomain).
     */
    public static RoomType reconstruct(Long id, String name, int totalRooms) {
        RoomType rt = new RoomType();
        rt.id = id;
        rt.name = name;
        rt.totalRooms = totalRooms;
        return rt;
    }

    public void addPrice(RoomTypePrice price) {
        price.assignTo(this);
        this.prices.add(price);
    }

    public Long getId()                          { return id; }
    public String getName()                      { return name; }
    public int getTotalRooms()                   { return totalRooms; }
    public List<RoomTypePrice> getPrices()       { return Collections.unmodifiableList(prices); }
}