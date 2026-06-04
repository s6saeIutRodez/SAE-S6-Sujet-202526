package fr.iut.rodez.hotel.infrastructure.metrics;

import fr.iut.rodez.hotel.domain.model.BookingStatus;
import fr.iut.rodez.hotel.domain.port.out.BookingRepository;
import fr.iut.rodez.hotel.domain.port.out.InventoryRepository;
import fr.iut.rodez.hotel.domain.port.out.InvoiceRepository;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Métriques Micrometer exposées via Prometheus (OTLP → OTel Collector → Prometheus).
 *
 * Conventions :
 *  - Micrometer  : dots    → hotel.bookings.all
 *  - Prometheus  : underscores → hotel_bookings_all
 *
 * ⚠️ Règle BigDecimal dans les lambdas Gauge :
 *   Si la lambda lève une exception (ou retourne NaN), Micrometer émet NaN.
 *   L'exportateur OTLP supprime les NaN → la métrique disparaît de Grafana.
 *   → TOUJOURS protéger les appels BigDecimal avec un bloc try/catch ou null-check.
 *   → Les adapters de repository retournent déjà BigDecimal.ZERO pour les cas null
 *     (voir BookingRepositoryAdapter, InvoiceRepositoryAdapter), mais on ajoute
 *     une garde supplémentaire ici par défense en profondeur.
 */
@Component
public class HotelMetrics {

    public HotelMetrics(BookingRepository bookingRepository,
                        RoomTypeRepository roomTypeRepository,
                        InventoryRepository inventoryRepository,
                        InvoiceRepository invoiceRepository,
                        MeterRegistry registry) {

        // ── RÉSERVATIONS ──────────────────────────────────────────────────────
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

        // ── CHIFFRE D'AFFAIRES ────────────────────────────────────────────────
        // null-check défensif : l'adaptateur retourne déjà BigDecimal.ZERO,
        // mais on garde la garde ici au cas où un autre adaptateur serait branché.
        Gauge.builder("hotel.revenue.total", bookingRepository,
                        repo -> {
                            BigDecimal rev = repo.sumRevenueByStatus(BookingStatus.CONFIRMED.name());
                            return (rev != null ? rev : BigDecimal.ZERO).doubleValue();
                        })
                .description("CA total des réservations confirmées (€ HT)")
                .baseUnit("EUR")
                .register(registry);

        // ── OCCUPATION — AUJOURD'HUI ──────────────────────────────────────────
        Gauge.builder("hotel.capacity.total", roomTypeRepository,
                        repo -> repo.findAll().stream()
                                .mapToInt(rt -> rt.getTotalRooms())
                                .sum())
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

        Gauge.builder("hotel.occupancy.rate", inventoryRepository,
                        repo -> {
                            double total    = repo.sumTotalRoomsByDate(LocalDate.now());
                            double reserved = repo.sumReservedRoomsByDate(LocalDate.now());
                            return total == 0 ? 0.0 : (reserved * 100.0 / total);
                        })
                .description("Taux d'occupation aujourd'hui (%)")
                .baseUnit("percent")
                .register(registry);

        // ── TYPES DE CHAMBRES ─────────────────────────────────────────────────
        Gauge.builder("hotel.room_types.count", roomTypeRepository,
                        repo -> repo.findAll().size())
                .description("Nombre de types de chambres configurés")
                .register(registry);

        // ── FACTURES ──────────────────────────────────────────────────────────
        Gauge.builder("hotel.invoices.total", invoiceRepository,
                        repo -> repo.countAll())
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

        // ── PRÉVISIONS D'OCCUPATION — 14 jours ───────────────────────────────
        // hotel_inventory_reserved_forecast{days_ahead="00"} = chambres réservées aujourd'hui
        // hotel_inventory_reserved_forecast{days_ahead="01"} = demain, ...
        //
        // Label zero-paddé → tri lexicographique correct dans Grafana (bar chart X-axis).
        // Lambda appellée à chaque scrape : LocalDate.now().plusDays(offset) est toujours frais.
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