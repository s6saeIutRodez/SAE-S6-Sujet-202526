package fr.iut.rodez.hotel.domain.service;

import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.model.RoomTypePrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PricingDomainService — Tests unitaires du service de tarification")
class PricingDomainServiceTest {

    // Pas de mock, pas de Spring — logique métier pure
    private PricingDomainService pricingService;
    private RoomType roomType;

    @BeforeEach
    void setUp() {
        pricingService = new PricingDomainService();

        roomType = RoomType.reconstruct(1L, "Chambre Simple", 10);

        // Tarifs :
        // Hiver  : 69 €/nuit  (jan-juin 2026)
        // Été    : 89 €/nuit  (juil-août 2026)
        // Automne: 69 €/nuit  (sep-déc 2026)
        roomType.addPrice(RoomTypePrice.create(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 6, 30),
                new BigDecimal("69.00")));

        roomType.addPrice(RoomTypePrice.create(
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 8, 31),
                new BigDecimal("89.00")));

        roomType.addPrice(RoomTypePrice.create(
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 12, 31),
                new BigDecimal("69.00")));
    }

    // ── getPriceForDate() ─────────────────────────────────────────────────────
    @Nested
    @DisplayName("getPriceForDate() — Tarif applicable à une date donnée")
    class GetPriceForDateTests {

        @Test
        @DisplayName("Doit retourner 69€ pour une date en hiver")
        void shouldReturnWinterPrice() {
            BigDecimal price = pricingService.getPriceForDate(roomType, LocalDate.of(2026, 3, 15));
            assertThat(price).isEqualByComparingTo(new BigDecimal("69.00"));
        }

        @Test
        @DisplayName("Doit retourner 89€ pour une date en été")
        void shouldReturnSummerPrice() {
            BigDecimal price = pricingService.getPriceForDate(roomType, LocalDate.of(2026, 7, 20));
            assertThat(price).isEqualByComparingTo(new BigDecimal("89.00"));
        }

        @Test
        @DisplayName("Doit retourner le bon tarif pour le premier jour d'une période")
        void shouldReturnPriceForFirstDayOfPeriod() {
            BigDecimal price = pricingService.getPriceForDate(roomType, LocalDate.of(2026, 7, 1));
            assertThat(price).isEqualByComparingTo(new BigDecimal("89.00"));
        }

        @Test
        @DisplayName("Doit retourner le bon tarif pour le dernier jour d'une période")
        void shouldReturnPriceForLastDayOfPeriod() {
            BigDecimal price = pricingService.getPriceForDate(roomType, LocalDate.of(2026, 8, 31));
            assertThat(price).isEqualByComparingTo(new BigDecimal("89.00"));
        }

        @Test
        @DisplayName("Doit lever IllegalStateException si aucun tarif défini pour la date")
        void shouldThrowIfNoPriceDefinedForDate() {
            // 2027 : aucun tarif configuré
            assertThatThrownBy(() ->
                pricingService.getPriceForDate(roomType, LocalDate.of(2027, 1, 1)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Aucun tarif");
        }
    }

    // ── calculateTotalPrice() ─────────────────────────────────────────────────
    @Nested
    @DisplayName("calculateTotalPrice() — Calcul du prix total du séjour")
    class CalculateTotalPriceTests {

        @Test
        @DisplayName("Doit calculer le prix pour 1 chambre sur 5 nuits en été (89€ × 5 = 445€)")
        void shouldCalculatePriceForOneRoomFiveNightsSummer() {
            BigDecimal total = pricingService.calculateTotalPrice(
                    roomType,
                    LocalDate.of(2026, 7, 10),
                    LocalDate.of(2026, 7, 15),
                    1);

            assertThat(total).isEqualByComparingTo(new BigDecimal("445.00"));
        }

        @Test
        @DisplayName("Doit multiplier par la quantité (89€ × 5 nuits × 2 chambres = 890€)")
        void shouldMultiplyByQuantity() {
            BigDecimal total = pricingService.calculateTotalPrice(
                    roomType,
                    LocalDate.of(2026, 7, 10),
                    LocalDate.of(2026, 7, 15),
                    2);

            assertThat(total).isEqualByComparingTo(new BigDecimal("890.00"));
        }

        @Test
        @DisplayName("Doit calculer correctement sur une seule nuit")
        void shouldCalculateForSingleNight() {
            BigDecimal total = pricingService.calculateTotalPrice(
                    roomType,
                    LocalDate.of(2026, 7, 10),
                    LocalDate.of(2026, 7, 11),
                    1);

            assertThat(total).isEqualByComparingTo(new BigDecimal("89.00"));
        }

        @Test
        @DisplayName("Doit calculer correctement un séjour à cheval sur deux périodes tarifaires")
        void shouldCalculatePriceAcrossTwoPricePeriods() {
            // 30 juin (69€) + 1 juillet (89€) = 158€ pour 1 chambre
            BigDecimal total = pricingService.calculateTotalPrice(
                    roomType,
                    LocalDate.of(2026, 6, 30),
                    LocalDate.of(2026, 7, 2),
                    1);

            // nuit du 30 juin : 69€, nuit du 1er juillet : 89€ → total 158€
            assertThat(total).isEqualByComparingTo(new BigDecimal("158.00"));
        }

        @Test
        @DisplayName("Doit lever IllegalStateException si une nuit n'a pas de tarif défini")
        void shouldThrowIfOneNightHasNoPrice() {
            assertThatThrownBy(() ->
                pricingService.calculateTotalPrice(
                    roomType,
                    LocalDate.of(2026, 12, 30),
                    LocalDate.of(2027, 1, 2), // 1er et 2 jan 2027 sans tarif
                    1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Aucun tarif");
        }
    }
}
