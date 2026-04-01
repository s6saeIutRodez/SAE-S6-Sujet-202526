package fr.iut.rodez.hotel.domain.api;

import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.RoomTypeRepository;
import org.springframework.http.HttpStatus;
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
    public List<RoomType> findAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public RoomType findById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Type introuvable : " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomType create(@RequestBody RoomTypeCreateRequest req) {
        return repository.save(RoomType.create(req.name(), req.totalRooms()));
    }

    @PutMapping("/{id}")
    public RoomType update(@PathVariable Long id, @RequestBody RoomTypeCreateRequest req) {
        RoomType existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Type introuvable : " + id));
        // Pour respecter l'information hiding : on recréerait l'entité
        // ou on exposerait une méthode update dans RoomType
        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { repository.deleteById(id); }

    public record RoomTypeCreateRequest(String name, int totalRooms) {}
}