package fr.adriencaubel.hotel.domain.api;


import fr.adriencaubel.hotel.domain.RoomType;
import fr.adriencaubel.hotel.domain.port.RoomTypeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room-types")
public class RoomTypeController {

    private final RoomTypeRepository repository;

    public RoomTypeController(RoomTypeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<RoomType> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public RoomType findById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @PostMapping
    public RoomType create(@RequestBody RoomType roomType) {
        return repository.save(roomType);
    }

    @PutMapping("/{id}")
    public RoomType update(@PathVariable Long id,
                           @RequestBody RoomType updated) {

        RoomType existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        existing.setName(updated.getName());
        existing.setTotalRooms(updated.getTotalRooms());

        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
