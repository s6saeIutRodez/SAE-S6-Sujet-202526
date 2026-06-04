package fr.iut.rodez.hotel.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Inventory — Tests unitaires de l'agrégat")
class InventoryTest {

    private static final LocalDate DATE = LocalDate.of(2026, 7, 10);

    private RoomType roomType() {
        return RoomType.reconstruct(1L, "Chambre Simple", 10);
    }

    // ── create() ──────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("create() — Création initiale")
    class CreateTests {

        @Test
        @DisplayName("Doit créer un inventaire avec 0 chambre réservée")
        void shouldCreateInventoryWithZeroReserved() {
            Inventory inv = Inventory.create(roomType(), DATE, 10);

            assertThat(inv.getTotalRooms()).isEqualTo(10);
            assertThat(inv.getReservedRooms()).isEqualTo(0);
            assertThat(inv.availableRooms()).isEqualTo(10);
            assertThat(inv.getDate()).isEqualTo(DATE);
        }
    }

    // ── reserve() ─────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("reserve() — Réservation de chambres")
    class ReserveTests {

        @Test
        @DisplayName("Doit réserver une chambre et mettre à jour le compteur")
        void shouldReserveOneRoom() {
            Inventory inv = Inventory.create(roomType(), DATE, 10);
            inv.reserve(1);

            assertThat(inv.getReservedRooms()).isEqualTo(1);
            assertThat(inv.availableRooms()).isEqualTo(9);
        }

        @Test
        @DisplayName("Doit réserver plusieurs chambres d'un coup")
        void shouldReserveMultipleRooms() {
            Inventory inv = Inventory.create(roomType(), DATE, 10);
            inv.reserve(5);

            assertThat(inv.getReservedRooms()).isEqualTo(5);
            assertThat(inv.availableRooms()).isEqualTo(5);
        }

        @Test
        @DisplayName("Doit permettre de réserver toutes les chambres")
        void shouldReserveAllRooms() {
            Inventory inv = Inventory.create(roomType(), DATE, 10);
            inv.reserve(10);

            assertThat(inv.availableRooms()).isEqualTo(0);
            assertThat(inv.getReservedRooms()).isEqualTo(10);
        }

        @Test
        @DisplayName("Doit lever IllegalStateException si sur-capacité")
        void shouldThrowIfOverbooking() {
            Inventory inv = Inventory.create(roomType(), DATE, 10);

            assertThatThrownBy(() -> inv.reserve(11))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("disponibles");
        }

        @Test
        @DisplayName("Doit lever IllegalStateException si plus aucune chambre disponible")
        void shouldThrowIfNoRoomAvailable() {
            Inventory inv = Inventory.create(roomType(), DATE, 10);
            inv.reserve(10);

            assertThatThrownBy(() -> inv.reserve(1))
                .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("Doit permettre des réservations successives tant que la capacité le permet")
        void shouldAllowSuccessiveReservations() {
            Inventory inv = Inventory.create(roomType(), DATE, 10);
            inv.reserve(3);
            inv.reserve(4);

            assertThat(inv.getReservedRooms()).isEqualTo(7);
            assertThat(inv.availableRooms()).isEqualTo(3);
        }
    }

    // ── release() ─────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("release() — Libération de chambres")
    class ReleaseTests {

        @Test
        @DisplayName("Doit libérer une chambre réservée")
        void shouldReleaseOneRoom() {
            Inventory inv = Inventory.reconstruct(1L, roomType(), DATE, 10, 5);
            inv.release(1);

            assertThat(inv.getReservedRooms()).isEqualTo(4);
            assertThat(inv.availableRooms()).isEqualTo(6);
        }

        @Test
        @DisplayName("Doit libérer toutes les chambres réservées")
        void shouldReleaseAllReservedRooms() {
            Inventory inv = Inventory.reconstruct(1L, roomType(), DATE, 10, 5);
            inv.release(5);

            assertThat(inv.getReservedRooms()).isEqualTo(0);
            assertThat(inv.availableRooms()).isEqualTo(10);
        }

        @Test
        @DisplayName("Doit lever IllegalStateException si libération supérieure aux réservations")
        void shouldThrowIfReleasingMoreThanReserved() {
            Inventory inv = Inventory.reconstruct(1L, roomType(), DATE, 10, 3);

            assertThatThrownBy(() -> inv.release(4))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("libérer");
        }

        @Test
        @DisplayName("Réserver puis libérer doit revenir à l'état initial")
        void reserveThenReleaseShouldRestoreInitialState() {
            Inventory inv = Inventory.create(roomType(), DATE, 10);
            inv.reserve(5);
            inv.release(5);

            assertThat(inv.getReservedRooms()).isEqualTo(0);
            assertThat(inv.availableRooms()).isEqualTo(10);
        }
    }

    // ── canReserve() ──────────────────────────────────────────────────────────
    @Nested
    @DisplayName("canReserve() — Vérification de disponibilité")
    class CanReserveTests {

        @Test
        @DisplayName("Doit retourner true si assez de chambres disponibles")
        void shouldReturnTrueIfEnoughRooms() {
            Inventory inv = Inventory.reconstruct(1L, roomType(), DATE, 10, 3);
            assertThat(inv.canReserve(5)).isTrue();
        }

        @Test
        @DisplayName("Doit retourner true pour une réservation exactement égale au disponible")
        void shouldReturnTrueIfExactlyEnough() {
            Inventory inv = Inventory.reconstruct(1L, roomType(), DATE, 10, 3);
            assertThat(inv.canReserve(7)).isTrue();
        }

        @Test
        @DisplayName("Doit retourner false si sur-capacité")
        void shouldReturnFalseIfOverbooking() {
            Inventory inv = Inventory.reconstruct(1L, roomType(), DATE, 10, 8);
            assertThat(inv.canReserve(3)).isFalse();
        }

        @Test
        @DisplayName("Doit retourner false si plus aucune chambre disponible")
        void shouldReturnFalseIfNoRoomAvailable() {
            Inventory inv = Inventory.reconstruct(1L, roomType(), DATE, 10, 10);
            assertThat(inv.canReserve(1)).isFalse();
        }
    }
}
