package fr.iut.rodez.hotel.infrastructure.metrics;

import fr.iut.rodez.hotel.domain.model.BookingStatus;
import fr.iut.rodez.hotel.domain.port.out.BookingRepository;
import fr.iut.rodez.hotel.domain.port.out.InventoryRepository;
import fr.iut.rodez.hotel.domain.port.out.InvoiceRepository;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class HotelMetrics {

    public HotelMetrics(BookingRepository bookingRepository,
                        RoomTypeRepository roomTypeRepository,
                        InventoryRepository inventoryRepository,
                        InvoiceRepository invoiceRepository,
                        MeterRegistry registry) {

        // ── RÉSERVATIONS ──────────────────────────────────────────────────
        Gauge.builder("hotel.bookings.total", bookingRepository,
                        repo -> repo.countByStatus(BookingStatus.CONFIRMED.name()))
                .description("Réservations confirmées")
                .tag("status", "CONFIRMED")
                .register(registry);

        Gauge.builder("hotel.bookings.total", bookingRepository,
                        repo -> repo.countByStatus(BookingStatus.PENDING.name()))
                .description("Réservations en attente")
                .tag("status", "PENDING")
                .register(registry);

        Gauge.builder("hotel.bookings.total", bookingRepository,
                        repo -> repo.countByStatus(BookingStatus.CANCELLED.name()))
                .description("Réservations annulées")
                .tag("status", "CANCELLED")
                .register(registry);

        Gauge.builder("hotel.bookings.all", bookingRepository,
                        repo -> repo.countAll())
                .description("Total toutes réservations confondues")
                .register(registry);

        // ── CHIFFRE D'AFFAIRES ────────────────────────────────────────────
        Gauge.builder("hotel.revenue.total", bookingRepository,
                        repo -> repo.sumRevenueByStatus(BookingStatus.CONFIRMED.name()).doubleValue())
                .description("CA total des réservations confirmées (€)")
                .baseUnit("EUR")
                .register(registry);

        // ── OCCUPATION ────────────────────────────────────────────────────
        Gauge.builder("hotel.capacity.total", roomTypeRepository,
                        repo -> repo.findAll().stream()
                                .mapToInt(rt -> rt.getTotalRooms())
                                .sum())
                .description("Capacité totale de l'hôtel")
                .register(registry);

        Gauge.builder("hotel.rooms.reserved.today", inventoryRepository,
                        repo -> repo.sumReservedRoomsByDate(LocalDate.now()))
                .description("Chambres réservées aujourd'hui")
                .register(registry);

        // Taux d'occupation en temps réel
        Gauge.builder("hotel.rooms.available.today", inventoryRepository,
                        repo -> repo.sumAvailableRoomsByDate(LocalDate.now()))
                .description("Chambres disponibles aujourd'hui")
                .register(registry);

        Gauge.builder("hotel.occupancy.rate", inventoryRepository,
                        repo -> {
                            double total = repo.sumTotalRoomsByDate(LocalDate.now());
                            double reserved = repo.sumReservedRoomsByDate(LocalDate.now());
                            return total == 0 ? 0.0 : (reserved * 100.0 / total);
                        })
                .description("Taux d'occupation aujourd'hui (%)")
                .baseUnit("percent")
                .register(registry);

        // ── TYPES DE CHAMBRES ─────────────────────────────────────────────
        Gauge.builder("hotel.room_types.count", roomTypeRepository,
                        repo -> repo.findAll().size())
                .description("Nombre de types de chambres configurés")
                .register(registry);

        // ── FACTURES ──────────────────────────────────────────────────────
        Gauge.builder("hotel.invoices.total", invoiceRepository,
                        repo -> repo.countAll())
                .description("Nombre total de factures émises")
                .register(registry);

        Gauge.builder("hotel.invoices.amount.ttc", invoiceRepository,
                        repo -> repo.sumTotalAmountTTC().doubleValue())
                .description("Montant total facturé TTC (€)")
                .baseUnit("EUR")
                .register(registry);
    }
}