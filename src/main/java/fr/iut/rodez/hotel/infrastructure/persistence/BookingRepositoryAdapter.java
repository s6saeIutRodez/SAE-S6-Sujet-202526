package fr.iut.rodez.hotel.infrastructure.persistence;

import fr.iut.rodez.hotel.domain.model.Booking;
import fr.iut.rodez.hotel.domain.port.out.BookingRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Adaptateur : traduit entre Booking (domaine) et BookingJpaEntity (infra).
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

    @Override
    public long countAll() {
        return bookingJpa.count();
    }

    /**
     * SUM retourne null si aucune réservation ne correspond au statut.
     * On retourne BigDecimal.ZERO dans ce cas pour éviter une NullPointerException
     * dans le Gauge Micrometer (qui émettrait NaN, supprimé par l'exportateur OTLP).
     */
    @Override
    public BigDecimal sumRevenueByStatus(String status) {
        BigDecimal result = bookingJpa.sumRevenueByStatus(status);
        return result != null ? result : BigDecimal.ZERO;
    }
}