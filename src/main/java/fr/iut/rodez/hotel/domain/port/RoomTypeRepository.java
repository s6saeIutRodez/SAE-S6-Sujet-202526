package fr.iut.rodez.hotel.domain.port;

import fr.iut.rodez.hotel.domain.model.RoomType;
import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository {
    RoomType save(RoomType roomType);
    Optional<RoomType> findById(Long id);
    List<RoomType> findAll();
    void deleteById(Long id);
}