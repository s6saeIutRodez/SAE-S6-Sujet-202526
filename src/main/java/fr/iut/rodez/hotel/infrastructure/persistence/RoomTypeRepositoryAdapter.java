package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomTypeRepositoryAdapter implements RoomTypeRepository {

    private final JpaRoomTypeRepository jpa;

    public RoomTypeRepositoryAdapter(JpaRoomTypeRepository jpa) {
        this.jpa = jpa;
    }

    @Override public RoomType save(RoomType rt) { return jpa.save(rt); }
    @Override public Optional<RoomType> findById(Long id) { return jpa.findById(id); }
    @Override public List<RoomType> findAll() { return jpa.findAll(); }
    @Override public void deleteById(Long id) { jpa.deleteById(id); }
}