package fr.iut.rodez.hotel.domain.port;

import fr.adriencaubel.hotel.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
