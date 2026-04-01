package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.Booking;
import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.BookingRepository;
import fr.iut.rodez.hotel.domain.port.EmailPort;
import fr.iut.rodez.hotel.domain.port.RoomTypeRepository;
import fr.iut.rodez.hotel.domain.service.AvailabilityDomainService;
import fr.iut.rodez.hotel.domain.service.PricingDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReserveRoomUseCase {

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
        this.roomTypeRepository = roomTypeRepository;
        this.bookingRepository = bookingRepository;
        this.availabilityService = availabilityService;
        this.pricingService = pricingService;
        this.emailPort = emailPort;
    }

    @Transactional
    public Booking execute(Command command) {

        RoomType roomType = roomTypeRepository.findById(command.roomTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Type de chambre introuvable : " + command.roomTypeId()));

        // Vérification de disponibilité via le Domain Service
        if (!availabilityService.isAvailable(roomType, command.from(), command.to(), command.quantity())) {
            throw new IllegalStateException("Pas assez de chambres disponibles sur cette période");
        }

        // Calcul du prix via le Domain Service
        BigDecimal amount = pricingService.calculateTotalPrice(
                roomType, command.from(), command.to(), command.quantity());

        // Création de la réservation via le constructeur métier (invariants vérifiés)
        Booking booking = Booking.create(
                roomType,
                command.from(), command.to(),
                command.quantity(), amount,
                command.nom(), command.prenom(), command.email()
        );

        // Ajout des options via la racine de l'agrégat
        if (command.options() != null) {
            for (String rawOption : command.options()) {
                if (rawOption == null || rawOption.isBlank()) continue;
                String[] parts = rawOption.split(",", 2);
                String type = parts[0].trim();
                String comment = parts.length == 2 ? parts[1].trim() : null;
                booking.addOption(type, comment);
            }
        }

        // Mise à jour de l'inventaire (Domain Service)
        availabilityService.reserveInventory(roomType, command.from(), command.to(), command.quantity());

        // Persistance
        Booking saved = bookingRepository.save(booking);

        // Action technique déléguée au port (pas de logique métier ici)
        emailPort.sendBookingConfirmation(saved.getEmail(), saved.getId());

        return saved;
    }

    // Command object : données d'entrée typées et immuables
    public record Command(
            Long roomTypeId,
            LocalDate from,
            LocalDate to,
            int quantity,
            String nom,
            String prenom,
            String email,
            List<String> options
    ) {}
}