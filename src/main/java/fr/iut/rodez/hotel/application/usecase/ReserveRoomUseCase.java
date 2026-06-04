package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.Booking;
import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.in.IReserveRoomUseCase;
import fr.iut.rodez.hotel.domain.port.out.BookingRepository;
import fr.iut.rodez.hotel.domain.port.out.EmailPort;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import fr.iut.rodez.hotel.domain.service.AvailabilityDomainService;
import fr.iut.rodez.hotel.domain.service.PricingDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class ReserveRoomUseCase implements IReserveRoomUseCase {

    private final RoomTypeRepository roomTypeRepository;
    private final BookingRepository bookingRepository;
    private final AvailabilityDomainService availabilityService;
    private final PricingDomainService pricingService;
    private final EmailPort emailPort;

    public ReserveRoomUseCase(RoomTypeRepository roomTypeRepository,
                              BookingRepository bookingRepository,
                              AvailabilityDomainService availabilityService,
                              PricingDomainService pricingService,
                              EmailPort emailPort) {
        this.roomTypeRepository  = roomTypeRepository;
        this.bookingRepository   = bookingRepository;
        this.availabilityService = availabilityService;
        this.pricingService      = pricingService;
        this.emailPort           = emailPort;
    }

    @Override
    @Transactional
    public Booking execute(IReserveRoomUseCase.Command command) {
        RoomType roomType = roomTypeRepository.findById(command.roomTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Type de chambre introuvable : " + command.roomTypeId()));

        if (!availabilityService.isAvailable(roomType, command.from(), command.to(), command.quantity())) {
            throw new IllegalStateException("Pas assez de chambres disponibles sur cette période");
        }

        BigDecimal amount = pricingService.calculateTotalPrice(
                roomType, command.from(), command.to(), command.quantity());

        Booking booking = Booking.create(
                roomType, command.from(), command.to(),
                command.quantity(), amount,
                command.nom(), command.prenom(), command.email());

        if (command.options() != null) {
            for (String rawOption : command.options()) {
                if (rawOption == null || rawOption.isBlank()) continue;
                String[] parts = rawOption.split(",", 2);
                booking.addOption(parts[0].trim(), parts.length == 2 ? parts[1].trim() : null);
            }
        }

        availabilityService.reserveInventory(roomType, command.from(), command.to(), command.quantity());

        Booking saved = bookingRepository.save(booking);
        emailPort.sendBookingConfirmation(saved.getEmail(), saved.getId());
        return saved;
    }
}