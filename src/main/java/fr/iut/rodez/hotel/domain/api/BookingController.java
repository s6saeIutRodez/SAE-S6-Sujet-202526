// infrastructure/api/BookingController.java
package fr.iut.rodez.hotel.domain.api;

import fr.iut.rodez.hotel.application.usecase.ReserveRoomUseCase;
import fr.iut.rodez.hotel.application.usecase.CancelBookingUseCase;
import fr.iut.rodez.hotel.domain.api.dto.BookingRequestDto;
import fr.iut.rodez.hotel.domain.api.dto.BookingResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final ReserveRoomUseCase reserveRoomUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;

    public BookingController(ReserveRoomUseCase reserveRoomUseCase,
                             CancelBookingUseCase cancelBookingUseCase) {
        this.reserveRoomUseCase = reserveRoomUseCase;
        this.cancelBookingUseCase = cancelBookingUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto reserve(@RequestBody @Valid BookingRequestDto req) {
        var command = new ReserveRoomUseCase.Command(
                req.roomTypeId(), req.from(), req.to(), req.quantity(),
                req.nom(), req.prenom(), req.email(), req.options()
        );
        return BookingResponseDto.from(reserveRoomUseCase.execute(command));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        cancelBookingUseCase.execute(id);
    }
}