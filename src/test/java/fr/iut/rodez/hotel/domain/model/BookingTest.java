package fr.iut.rodez.hotel.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Booking — Tests unitaires de l'agrégat")
class BookingTest {

    // ── Données de test ───────────────────────────────────────────────────────
    private static final LocalDate FROM     = LocalDate.of(2026, 7, 10);
    private static final LocalDate TO       = LocalDate.of(2026, 7, 15);
    private static final BigDecimal AMOUNT  = new BigDecimal("445.00");

    private RoomType roomType() {
        return RoomType.reconstruct(1L, "Chambre Simple", 10);
    }

    private Booking validBooking() {
        return Booking.create(roomType(), FROM, TO, 1, AMOUNT, "Brouzes", "Alexandre", "a@test.com");
    }

    // ── create() ──────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("create() — Fabrique statique & invariants")
    class CreateTests {

        @Test
        @DisplayName("Doit créer une réservation valide avec statut CONFIRMED")
        void shouldCreateValidBookingWithConfirmedStatus() {
            Booking booking = validBooking();

            assertThat(booking.getRoomType().getName()).isEqualTo("Chambre Simple");
            assertThat(booking.getFromDate()).isEqualTo(FROM);
            assertThat(booking.getToDate()).isEqualTo(TO);
            assertThat(booking.getQuantity()).isEqualTo(1);
            assertThat(booking.getAmount()).isEqualByComparingTo(AMOUNT);
            assertThat(booking.getStatus()).isEqualTo(BookingStatus.CONFIRMED.name());
            assertThat(booking.getNom()).isEqualTo("Brouzes");
            assertThat(booking.getPrenom()).isEqualTo("Alexandre");
            assertThat(booking.getEmail()).isEqualTo("a@test.com");
        }

        @Test
        @DisplayName("Doit refuser si la date de départ est avant la date d'arrivée")
        void shouldRejectIfToBeforeFrom() {
            assertThatThrownBy(() ->
                Booking.create(roomType(), TO, FROM, 1, AMOUNT, "A", "B", "c@test.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date de départ");
        }

        @Test
        @DisplayName("Doit refuser si les dates d'arrivée et départ sont identiques")
        void shouldRejectIfFromEqualsTo() {
            assertThatThrownBy(() ->
                Booking.create(roomType(), FROM, FROM, 1, AMOUNT, "A", "B", "c@test.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date de départ");
        }

        @Test
        @DisplayName("Doit refuser si la quantité est zéro")
        void shouldRejectIfQuantityIsZero() {
            assertThatThrownBy(() ->
                Booking.create(roomType(), FROM, TO, 0, AMOUNT, "A", "B", "c@test.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantité");
        }

        @Test
        @DisplayName("Doit refuser si la quantité est négative")
        void shouldRejectIfQuantityIsNegative() {
            assertThatThrownBy(() ->
                Booking.create(roomType(), FROM, TO, -1, AMOUNT, "A", "B", "c@test.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantité");
        }

        @Test
        @DisplayName("Doit refuser si le montant est nul")
        void shouldRejectIfAmountIsNull() {
            assertThatThrownBy(() ->
                Booking.create(roomType(), FROM, TO, 1, null, "A", "B", "c@test.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("montant");
        }

        @Test
        @DisplayName("Doit refuser si le montant est négatif")
        void shouldRejectIfAmountIsNegative() {
            assertThatThrownBy(() ->
                Booking.create(roomType(), FROM, TO, 1, new BigDecimal("-1"), "A", "B", "c@test.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("montant");
        }

        @Test
        @DisplayName("Doit accepter un montant à zéro (offert)")
        void shouldAcceptZeroAmount() {
            Booking booking = Booking.create(roomType(), FROM, TO, 1, BigDecimal.ZERO, "A", "B", "c@test.com");
            assertThat(booking.getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    // ── cancel() ──────────────────────────────────────────────────────────────
    @Nested
    @DisplayName("cancel() — Annulation & invariant")
    class CancelTests {

        @Test
        @DisplayName("Doit passer le statut à CANCELLED")
        void shouldSetStatusToCancelled() {
            Booking booking = validBooking();
            booking.cancel();
            assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELLED.name());
        }

        @Test
        @DisplayName("Doit lever IllegalStateException si déjà annulée")
        void shouldThrowIfAlreadyCancelled() {
            Booking booking = validBooking();
            booking.cancel();

            assertThatThrownBy(booking::cancel)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("déjà annulée");
        }
    }

    // ── addOption() ───────────────────────────────────────────────────────────
    @Nested
    @DisplayName("addOption() — Options de réservation")
    class AddOptionTests {

        @Test
        @DisplayName("Doit ajouter une option avec commentaire")
        void shouldAddOptionWithComment() {
            Booking booking = validBooking();
            booking.addOption("ANNIVERSAIRE", "Gâteau surprise");

            assertThat(booking.getOptions()).hasSize(1);
            assertThat(booking.getOptions().get(0).getType()).isEqualTo("ANNIVERSAIRE");
            assertThat(booking.getOptions().get(0).getComment()).isEqualTo("Gâteau surprise");
        }

        @Test
        @DisplayName("Doit ajouter une option sans commentaire")
        void shouldAddOptionWithoutComment() {
            Booking booking = validBooking();
            booking.addOption("LIT BEBE", null);

            assertThat(booking.getOptions()).hasSize(1);
            assertThat(booking.getOptions().get(0).getComment()).isNull();
        }

        @Test
        @DisplayName("Doit ajouter plusieurs options")
        void shouldAddMultipleOptions() {
            Booking booking = validBooking();
            booking.addOption("ANNIVERSAIRE", "Déco");
            booking.addOption("FLEUR", "Roses rouges");

            assertThat(booking.getOptions()).hasSize(2);
        }

        @Test
        @DisplayName("Doit refuser un type d'option vide")
        void shouldRejectBlankOptionType() {
            Booking booking = validBooking();

            assertThatThrownBy(() -> booking.addOption("", "commentaire"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type d'option");
        }

        @Test
        @DisplayName("Doit refuser un type d'option null")
        void shouldRejectNullOptionType() {
            Booking booking = validBooking();

            assertThatThrownBy(() -> booking.addOption(null, "commentaire"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("type d'option");
        }

        @Test
        @DisplayName("La liste des options doit être non modifiable depuis l'extérieur")
        void optionListShouldBeUnmodifiable() {
            Booking booking = validBooking();

            assertThatThrownBy(() -> booking.getOptions().add(new BookingOption(booking, "X", null)))
                .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    // ── reconstruct() ─────────────────────────────────────────────────────────
    @Nested
    @DisplayName("reconstruct() — Reconstitution depuis la persistance")
    class ReconstructTests {

        @Test
        @DisplayName("Doit reconstituer une réservation annulée sans lever d'exception")
        void shouldReconstructCancelledBookingWithoutException() {
            Booking booking = Booking.reconstruct(
                42L, roomType(), FROM, TO, 2,
                AMOUNT, BookingStatus.CANCELLED.name(),
                "Martin", "Alice", "alice@test.com"
            );

            assertThat(booking.getId()).isEqualTo(42L);
            assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELLED.name());
        }

        @Test
        @DisplayName("Doit refuser cancel() sur une réservation reconstituée déjà annulée")
        void shouldThrowCancelOnReconstructedCancelled() {
            Booking booking = Booking.reconstruct(
                1L, roomType(), FROM, TO, 1,
                AMOUNT, BookingStatus.CANCELLED.name(),
                "A", "B", "c@test.com"
            );

            assertThatThrownBy(booking::cancel)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("déjà annulée");
        }
    }
}
