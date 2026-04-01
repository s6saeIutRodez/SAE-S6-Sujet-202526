package fr.iut.rodez.hotel.domain.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "room_types")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int totalRooms;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RoomTypePrice> prices = new ArrayList<>();

    protected RoomType() {}

    public static RoomType create(String name, int totalRooms) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom du type de chambre est obligatoire");
        }
        if (totalRooms <= 0) {
            throw new IllegalArgumentException("Le nombre de chambres doit être positif");
        }
        RoomType rt = new RoomType();
        rt.name = name;
        rt.totalRooms = totalRooms;
        return rt;
    }

    public void addPrice(RoomTypePrice price) {
        price.assignTo(this);
        this.prices.add(price);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getTotalRooms() { return totalRooms; }
    public List<RoomTypePrice> getPrices() { return Collections.unmodifiableList(prices); }
}