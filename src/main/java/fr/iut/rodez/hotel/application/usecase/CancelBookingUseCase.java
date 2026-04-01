package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.Booking;
import fr.iut.rodez.hotel.domain.port.BookingRepository;
import fr.iut.rodez.hotel.domain.service.AvailabilityDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelBookingUseCase {

    private final BookingRepository bookingRepository;
    private final AvailabilityDomainService availabilityService;

    public CancelBookingUseCase(BookingRepository bookingRepository,
                                AvailabilityDomainService availabilityService) {
        this.bookingRepository = bookingRepository;
        this.availabilityService = availabilityService;
    }

    @Transactional
    public void execute(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Réservation introuvable : " + bookingId));

        booking.cancel(); // invariant vérifié dans l'agrégat

        availabilityService.releaseInventory(
                booking.getRoomType(),
                booking.getFromDate(),
                booking.getToDate(),
                booking.getQuantity()
        );

        bookingRepository.save(booking);
    }
}