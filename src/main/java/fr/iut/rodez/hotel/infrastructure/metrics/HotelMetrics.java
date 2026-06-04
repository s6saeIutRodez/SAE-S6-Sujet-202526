package fr.iut.rodez.hotel.infrastructure.metrics;

import fr.iut.rodez.hotel.domain.model.BookingStatus;
import fr.iut.rodez.hotel.domain.port.in.IGetDashboardUseCase;
import fr.iut.rodez.hotel.domain.port.out.InventoryRepository;
import fr.iut.rodez.hotel.domain.port.out.InvoiceRepository;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class HotelMetrics {

    public HotelMetrics(IGetDashboardUseCase getDashboardUseCase,
                        RoomTypeRepository roomTypeRepository,
                        InventoryRepository inventoryRepository,
                        InvoiceRepository invoiceRepository,
                        MeterRegistry registry) {

        // ── CHIFFRE D'AFFAIRES VIA LE DASHBOARD USE CASE ──────────────────────
        Gauge.builder("hotel.revenue.total", getDashboardUseCase,
                        uc -> {
                            BigDecimal rev = uc.execute().totalRevenue(); // ou getTotalRevenue()
                            return (rev != null ? rev : BigDecimal.ZERO).doubleValue();
                        })
                .description("CA total des réservations confirmées (€ HT)")
                .baseUnit("EUR")
                .register(registry);

        // ── RÉSERVATIONS VIA LE DASHBOARD USE CASE ────────────────────────────
        Gauge.builder("hotel.bookings.all", getDashboardUseCase,
                        uc -> (double) uc.execute().totalBookings()) // ou getTotalBookings()
                .description("Total toutes réservations confondues")
                .register(registry);

        // Dynamisation des jauges par statut
        for (BookingStatus status : BookingStatus.values()) {
            Gauge.builder("hotel.bookings.total", getDashboardUseCase,
                            uc -> {
                                var statsByStatus = uc.execute().bookingsByStatus(); // ou getBookingsByStatus()
                                return statsByStatus != null ? statsByStatus.getOrDefault(status.name(), 0L).doubleValue() : 0.0;
                            })
                    .description("Réservations enregistrées par statut")
                    .tag("status", status.name())
                    .register(registry);
        }

        // ── OCCUPATION ET CAPACITÉ ───────────────────────────────────────────
        Gauge.builder("hotel.capacity.total", roomTypeRepository,
                        repo -> repo.findAll().stream().mapToInt(rt -> rt.getTotalRooms()).sum())
                .description("Capacité totale de l'hôtel (toutes chambres)")
                .register(registry);

        Gauge.builder("hotel.rooms.reserved.today", inventoryRepository,
                        repo -> repo.sumReservedRoomsByDate(LocalDate.now()))
                .description("Chambres réservées aujourd'hui")
                .register(registry);

        Gauge.builder("hotel.rooms.available.today", inventoryRepository,
                        repo -> repo.sumAvailableRoomsByDate(LocalDate.now()))
                .description("Chambres disponibles aujourd'hui")
                .register(registry);

        // Simplifié : on réutilise directement le taux calculé par votre Use Case
        Gauge.builder("hotel.occupancy.rate", getDashboardUseCase,
                        uc -> uc.execute().occupancyRate()) // ou getOccupancyRate()
                .description("Taux d'occupation aujourd'hui (%)")
                .baseUnit("percent")
                .register(registry);

        // ── TYPES DE CHAMBRES ─────────────────────────────────────────────────
        Gauge.builder("hotel.room_types.count", roomTypeRepository, repo -> repo.findAll().size())
                .description("Nombre de types de chambres configurés")
                .register(registry);

        // ── FACTURES ──────────────────────────────────────────────────────────
        Gauge.builder("hotel.invoices.total", invoiceRepository, InvoiceRepository::countAll)
                .description("Nombre total de factures émises")
                .register(registry);

        Gauge.builder("hotel.invoices.amount.ttc", invoiceRepository,
                        repo -> {
                            BigDecimal ttc = repo.sumTotalAmountTTC();
                            return (ttc != null ? ttc : BigDecimal.ZERO).doubleValue();
                        })
                .description("Montant total facturé TTC (€)")
                .baseUnit("EUR")
                .register(registry);

        // ── PRÉVISIONS D'OCCUPATION (Si conservées dans Prometheus) ───────────
        for (int daysAhead = 0; daysAhead <= 13; daysAhead++) {
            final int offset = daysAhead;
            final String label = String.format("%02d", daysAhead);

            Gauge.builder("hotel.inventory.reserved.forecast", inventoryRepository,
                            repo -> (double) repo.sumReservedRoomsByDate(LocalDate.now().plusDays(offset)))
                    .description("Chambres réservées dans " + daysAhead + " jour(s)")
                    .tag("days_ahead", label)
                    .register(registry);

            Gauge.builder("hotel.inventory.total.forecast", inventoryRepository,
                            repo -> (double) repo.sumTotalRoomsByDate(LocalDate.now().plusDays(offset)))
                    .description("Capacité totale dans " + daysAhead + " jour(s)")
                    .tag("days_ahead", label)
                    .register(registry);
        }
    }
}