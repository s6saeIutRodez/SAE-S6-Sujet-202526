// infrastructure/persistence/JpaRoomTypeRepository.java  
package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaRoomTypeRepository extends JpaRepository<RoomType, Long> {}