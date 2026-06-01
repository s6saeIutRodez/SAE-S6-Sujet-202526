package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.Booking;
import fr.iut.rodez.hotel.domain.port.out.BookingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Adaptateur : traduit entre Booking (domaine) et BookingJpaEntity (infra).
 *
 * Injecte JpaRoomTypeRepository pour obtenir une référence FK vers le room type
 * via getReferenceById() — évite un SELECT inutile tout en satisfaisant Hibernate.
 */
@Repository
public class BookingRepositoryAdapter implements BookingRepository {

    private final JpaBookingRepository bookingJpa;
    private final JpaRoomTypeRepository roomTypeJpa;

    public BookingRepositoryAdapter(JpaBookingRepository bookingJpa,
                                    JpaRoomTypeRepository roomTypeJpa) {
        this.bookingJpa  = bookingJpa;
        this.roomTypeJpa = roomTypeJpa;
    }

    @Override
    public Booking save(Booking booking) {
        // getReferenceById : crée un proxy FK sans déclencher de SELECT sur room_types
        RoomTypeJpaEntity roomTypeRef = roomTypeJpa.getReferenceById(booking.getRoomType().getId());
        BookingJpaEntity entity = BookingJpaEntity.fromDomain(booking, roomTypeRef);
        return bookingJpa.save(entity).toDomain();
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingJpa.findById(id).map(BookingJpaEntity::toDomain);
    }

    @Override
    public List<Booking> findAll() {
        return bookingJpa.findAll().stream()
                .map(BookingJpaEntity::toDomain)
                .toList();
    }
}