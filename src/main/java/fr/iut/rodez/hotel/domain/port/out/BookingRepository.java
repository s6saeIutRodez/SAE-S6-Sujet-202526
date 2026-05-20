package fr.iut.rodez.hotel.domain.port.out;

import fr.iut.rodez.hotel.domain.model.Booking;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);
    Optional<Booking> findById(Long id);
    List<Booking> findAll();
}