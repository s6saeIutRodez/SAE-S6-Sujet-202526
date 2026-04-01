// infrastructure/persistence/BookingRepositoryAdapter.java
package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.Booking;
import fr.iut.rodez.hotel.domain.port.BookingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class BookingRepositoryAdapter implements BookingRepository {

    private final JpaBookingRepository jpa;

    public BookingRepositoryAdapter(JpaBookingRepository jpa) {
        this.jpa = jpa;
    }

    @Override public Booking save(Booking booking) { return jpa.save(booking); }
    @Override public Optional<Booking> findById(Long id) { return jpa.findById(id); }
    @Override public List<Booking> findAll() { return jpa.findAll(); }
}