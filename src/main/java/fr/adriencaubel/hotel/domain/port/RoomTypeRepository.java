package fr.adriencaubel.hotel.domain.port;

import fr.adriencaubel.hotel.domain.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
}
