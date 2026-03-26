package fr.adriencaubel.hotel.domain.port;

import fr.adriencaubel.hotel.domain.RoomTypePrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomTypeRateRepository
        extends JpaRepository<RoomTypePrice, Long> {

    List<RoomTypePrice> findByRoomTypeId(Long roomTypeId);
}