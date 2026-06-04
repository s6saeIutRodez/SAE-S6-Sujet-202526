package fr.iut.rodez.hotel.domain.service;

import fr.iut.rodez.hotel.domain.model.Inventory;
import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.out.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AvailabilityDomainService — Tests unitaires du service de disponibilité")
class AvailabilityDomainServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private AvailabilityDomainService availabilityService;

    private RoomType roomType;

    private static final LocalDate FROM = LocalDate.of(2026, 7, 10);
    private static final LocalDate TO   = LocalDate.of(2026, 7, 13); // 3 nuits

    @BeforeEach
    void setUp() {
        roomType = RoomType.reconstruct(1L, "Chambre Simple", 10);
    }

    // ── getMinAvailableRooms() ────────────────────────────────────────────────
    @Nested
    @DisplayName("getMinAvailableRooms() — Calcul du minimum disponible sur la période")
    class GetMinAvailableRoomsTests {

        @Test
        @DisplayName("Doit retourner totalRooms si aucun inventaire existant")
        void shouldReturnTotalRoomsIfNoInventoryExists() {
            when(inventoryRepository.findByRoomTypeIdAndDateBetween(1L, FROM, TO))
                .thenReturn(List.of());

            int result = availabilityService.getMinAvailableRooms(roomType, FROM, TO);

            assertThat(result).isEqualTo(10);
        }

        @Test
        @DisplayName("Doit retourner le minimum des chambres disponibles sur la période")
        void shouldReturnMinimumAvailableAcrossPeriod() {
            // J+0 : 8 dispo, J+1 : 5 dispo (le min), J+2 : 9 dispo
            List<Inventory> inventories = List.of(
                Inventory.reconstruct(1L, roomType, FROM,           10, 2),
                Inventory.reconstruct(2L, roomType, FROM.plusDays(1), 10, 5),
                Inventory.reconstruct(3L, roomType, FROM.plusDays(2), 10, 1)
            );

            when(inventoryRepository.findByRoomTypeIdAndDateBetween(1L, FROM, TO))
                .thenReturn(inventories);

            int result = availabilityService.getMinAvailableRooms(roomType, FROM, TO);

            assertThat(result).isEqualTo(5); // 10 - 5 = 5 minimum
        }

        @Test
        @DisplayName("Doit retourner 0 si une nuit est complètement pleine")
        void shouldReturnZeroIfOneNightIsFullyBooked() {
            List<Inventory> inventories = List.of(
                Inventory.reconstruct(1L, roomType, FROM,           10, 3),
                Inventory.reconstruct(2L, roomType, FROM.plusDays(1), 10, 10), // plein
                Inventory.reconstruct(3L, roomType, FROM.plusDays(2), 10, 2)
            );

            when(inventoryRepository.findByRoomTypeIdAndDateBetween(1L, FROM, TO))
                .thenReturn(inventories);

            int result = availabilityService.getMinAvailableRooms(roomType, FROM, TO);

            assertThat(result).isEqualTo(0);
        }
    }

    // ── isAvailable() ─────────────────────────────────────────────────────────
    @Nested
    @DisplayName("isAvailable() — Vérification de disponibilité")
    class IsAvailableTests {

        @Test
        @DisplayName("Doit retourner true si assez de chambres disponibles")
        void shouldReturnTrueIfEnoughRoomsAvailable() {
            when(inventoryRepository.findByRoomTypeIdAndDateBetween(1L, FROM, TO))
                .thenReturn(List.of(
                    Inventory.reconstruct(1L, roomType, FROM, 10, 2)
                ));

            assertThat(availabilityService.isAvailable(roomType, FROM, TO, 5)).isTrue();
        }

        @Test
        @DisplayName("Doit retourner false si pas assez de chambres disponibles")
        void shouldReturnFalseIfNotEnoughRoomsAvailable() {
            when(inventoryRepository.findByRoomTypeIdAndDateBetween(1L, FROM, TO))
                .thenReturn(List.of(
                    Inventory.reconstruct(1L, roomType, FROM, 10, 8) // seulement 2 dispo
                ));

            assertThat(availabilityService.isAvailable(roomType, FROM, TO, 5)).isFalse();
        }

        @Test
        @DisplayName("Doit retourner true si aucun inventaire (capacité totale disponible)")
        void shouldReturnTrueIfNoInventoryExists() {
            when(inventoryRepository.findByRoomTypeIdAndDateBetween(1L, FROM, TO))
                .thenReturn(List.of());

            assertThat(availabilityService.isAvailable(roomType, FROM, TO, 10)).isTrue();
        }

        @Test
        @DisplayName("Doit retourner false si demande supérieure à la capacité totale")
        void shouldReturnFalseIfRequestExceedsTotalCapacity() {
            when(inventoryRepository.findByRoomTypeIdAndDateBetween(1L, FROM, TO))
                .thenReturn(List.of());

            assertThat(availabilityService.isAvailable(roomType, FROM, TO, 11)).isFalse();
        }
    }

    // ── reserveInventory() ────────────────────────────────────────────────────
    @Nested
    @DisplayName("reserveInventory() — Réservation de l'inventaire")
    class ReserveInventoryTests {

        @Test
        @DisplayName("Doit appeler save() pour chaque nuit de la période")
        void shouldSaveInventoryForEachNight() {
            // 3 nuits : FROM, FROM+1, FROM+2
            when(inventoryRepository.findByRoomTypeIdAndDate(eq(1L), any()))
                .thenReturn(Optional.of(Inventory.reconstruct(1L, roomType, FROM, 10, 0)));

            when(inventoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            availabilityService.reserveInventory(roomType, FROM, TO, 2);

            // save() appelé 3 fois (une par nuit : 10, 11, 12 juillet)
            verify(inventoryRepository, times(3)).save(any(Inventory.class));
        }

        @Test
        @DisplayName("Doit créer un inventaire si inexistant pour la date")
        void shouldCreateInventoryIfNotExists() {
            when(inventoryRepository.findByRoomTypeIdAndDate(eq(1L), any()))
                .thenReturn(Optional.empty());

            when(inventoryRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

            availabilityService.reserveInventory(roomType, FROM, TO, 1);

            // findByRoomTypeIdAndDate et save appelés pour les 3 nuits
            verify(inventoryRepository, times(3)).findByRoomTypeIdAndDate(eq(1L), any());
            verify(inventoryRepository, times(6)).save(any()); // 3 créations + 3 mises à jour
        }
    }

    // ── releaseInventory() ────────────────────────────────────────────────────
    @Nested
    @DisplayName("releaseInventory() — Libération de l'inventaire")
    class ReleaseInventoryTests {

        @Test
        @DisplayName("Doit libérer les chambres pour chaque nuit de la période")
        void shouldReleaseRoomsForEachNight() {
            Inventory inv1 = Inventory.reconstruct(1L, roomType, FROM,             10, 3);
            Inventory inv2 = Inventory.reconstruct(2L, roomType, FROM.plusDays(1), 10, 3);
            Inventory inv3 = Inventory.reconstruct(3L, roomType, FROM.plusDays(2), 10, 3);

            when(inventoryRepository.findByRoomTypeIdAndDate(1L, FROM))
                .thenReturn(Optional.of(inv1));
            when(inventoryRepository.findByRoomTypeIdAndDate(1L, FROM.plusDays(1)))
                .thenReturn(Optional.of(inv2));
            when(inventoryRepository.findByRoomTypeIdAndDate(1L, FROM.plusDays(2)))
                .thenReturn(Optional.of(inv3));

            when(inventoryRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            availabilityService.releaseInventory(roomType, FROM, TO, 2);

            verify(inventoryRepository, times(3)).save(any(Inventory.class));
            // Chaque inventaire doit avoir 1 chambre réservée restante (3 - 2)
            assertThat(inv1.getReservedRooms()).isEqualTo(1);
            assertThat(inv2.getReservedRooms()).isEqualTo(1);
            assertThat(inv3.getReservedRooms()).isEqualTo(1);
        }

        @Test
        @DisplayName("Doit ignorer silencieusement si l'inventaire n'existe pas pour une date")
        void shouldIgnoreIfNoInventoryForDate() {
            when(inventoryRepository.findByRoomTypeIdAndDate(any(), any()))
                .thenReturn(Optional.empty());

            // Ne doit pas lever d'exception
            assertThatNoException().isThrownBy(() ->
                availabilityService.releaseInventory(roomType, FROM, TO, 2));

            verify(inventoryRepository, never()).save(any());
        }
    }
}
