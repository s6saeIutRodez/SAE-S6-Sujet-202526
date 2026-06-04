package fr.iut.rodez.hotel.domain.service;

import fr.iut.rodez.hotel.domain.model.Inventory;
import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.out.InventoryRepository;
import java.time.LocalDate;
import java.util.List;

public class AvailabilityDomainService {

    private final InventoryRepository inventoryRepository;

    public AvailabilityDomainService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public int getMinAvailableRooms(RoomType roomType, LocalDate from, LocalDate to) {
        List<Inventory> inventories = inventoryRepository
                .findByRoomTypeIdAndDateBetween(roomType.getId(), from, to);

        if (inventories.isEmpty()) {
            return roomType.getTotalRooms();
        }

        return inventories.stream()
                .mapToInt(Inventory::availableRooms)
                .min()
                .orElse(roomType.getTotalRooms());
    }

    public boolean isAvailable(RoomType roomType, LocalDate from, LocalDate to, int quantity) {
        return getMinAvailableRooms(roomType, from, to) >= quantity;
    }

    public void reserveInventory(RoomType roomType, LocalDate from, LocalDate to, int quantity) {
        LocalDate current = from;
        while (current.isBefore(to)) {
            Inventory inventory = getOrCreateInventory(roomType, current);
            inventory.reserve(quantity);
            inventoryRepository.save(inventory);
            current = current.plusDays(1);
        }
    }

    public void releaseInventory(RoomType roomType, LocalDate from, LocalDate to, int quantity) {
        LocalDate current = from;
        while (current.isBefore(to)) {
            inventoryRepository.findByRoomTypeIdAndDate(roomType.getId(), current)
                    .ifPresent(inv -> {
                        inv.release(quantity);
                        inventoryRepository.save(inv);
                    });
            current = current.plusDays(1);
        }
    }

    private Inventory getOrCreateInventory(RoomType roomType, LocalDate date) {
        return inventoryRepository
                .findByRoomTypeIdAndDate(roomType.getId(), date)
                .orElseGet(() -> {
                    Inventory inv = Inventory.create(roomType, date, roomType.getTotalRooms());
                    return inventoryRepository.save(inv);
                });
    }
}
