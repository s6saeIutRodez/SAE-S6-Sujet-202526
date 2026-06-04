package fr.iut.rodez.hotel.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DisplayName("RoomType — Tests unitaires de l'agrégat")
class RoomTypeTest {

    // ── create() ──────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("create() — Fabrique statique & invariants")
    class CreateTests {

        @Test
        @DisplayName("Doit créer un type de chambre valide")
        void shouldCreateValidRoomType() {
            RoomType rt = RoomType.create("Suite", 5);

            assertThat(rt.getName()).isEqualTo("Suite");
            assertThat(rt.getTotalRooms()).isEqualTo(5);
            assertThat(rt.getPrices()).isEmpty();
        }

        @Test
        @DisplayName("Doit refuser un nom null")
        void shouldRejectNullName() {
            assertThatThrownBy(() -> RoomType.create(null, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nom");
        }

        @Test
        @DisplayName("Doit refuser un nom vide")
        void shouldRejectBlankName() {
            assertThatThrownBy(() -> RoomType.create("  ", 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nom");
        }

        @Test
        @DisplayName("Doit refuser totalRooms à zéro")
        void shouldRejectZeroTotalRooms() {
            assertThatThrownBy(() -> RoomType.create("Suite", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("chambres");
        }

        @Test
        @DisplayName("Doit refuser totalRooms négatif")
        void shouldRejectNegativeTotalRooms() {
            assertThatThrownBy(() -> RoomType.create("Suite", -3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("chambres");
        }
    }

    // ── addPrice() ─────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("addPrice() — Ajout de tarifs")
    class AddPriceTests {

        @Test
        @DisplayName("Doit ajouter un tarif valide")
        void shouldAddValidPrice() {
            RoomType rt = RoomType.create("Suite", 5);
            RoomTypePrice price = RoomTypePrice.create(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 6, 30),
                new BigDecimal("299.00"));

            rt.addPrice(price);

            assertThat(rt.getPrices()).hasSize(1);
            assertThat(rt.getPrices().get(0).getPricePerNight())
                .isEqualByComparingTo(new BigDecimal("299.00"));
        }

        @Test
        @DisplayName("Doit accepter plusieurs tarifs sur des périodes différentes")
        void shouldAddMultiplePricesOnDifferentPeriods() {
            RoomType rt = RoomType.create("Suite", 5);
            rt.addPrice(RoomTypePrice.create(
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30), new BigDecimal("69.00")));
            rt.addPrice(RoomTypePrice.create(
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 8, 31), new BigDecimal("89.00")));

            assertThat(rt.getPrices()).hasSize(2);
        }

        @Test
        @DisplayName("La liste des tarifs doit être non modifiable depuis l'extérieur")
        void priceListShouldBeUnmodifiable() {
            RoomType rt = RoomType.create("Suite", 5);

            assertThatThrownBy(() -> rt.getPrices().add(
                RoomTypePrice.create(
                    LocalDate.of(2026, 1, 1),
                    LocalDate.of(2026, 6, 30),
                    new BigDecimal("99.00"))))
                .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
