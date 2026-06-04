package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.Booking;
import fr.iut.rodez.hotel.domain.port.out.BookingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    @Override
    public long countByStatus(String status) {
        return bookingJpa.countByStatus(status);
    }

    // ── IMPLÉMENTATION DES MÉTRIQUES OPTIMISÉES ──
    @Override
    public long countAll() {
        return bookingJpa.count(); // Utilise la méthode count() native de Spring Data
    }

    @Override
    public BigDecimal sumRevenueByStatus(String status) {
        return bookingJpa.sumRevenueByStatus(status);
    }
}