package fr.iut.rodez.hotel.infrastructure.api;

import fr.iut.rodez.hotel.domain.port.in.ICancelBookingUseCase;
import fr.iut.rodez.hotel.domain.port.in.ICalculatePriceUseCase;
import fr.iut.rodez.hotel.domain.port.in.IReserveRoomUseCase;
import fr.iut.rodez.hotel.infrastructure.api.dto.BookingRequestDto;
import fr.iut.rodez.hotel.infrastructure.api.dto.BookingResponseDto;
import fr.iut.rodez.hotel.infrastructure.api.dto.PriceEstimateResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final IReserveRoomUseCase reserveRoomUseCase;
    private final ICancelBookingUseCase cancelBookingUseCase;
    private final ICalculatePriceUseCase calculatePriceUseCase;

    public BookingController(IReserveRoomUseCase reserveRoomUseCase,
                             ICancelBookingUseCase cancelBookingUseCase,
                             ICalculatePriceUseCase calculatePriceUseCase) {
        this.reserveRoomUseCase    = reserveRoomUseCase;
        this.cancelBookingUseCase  = cancelBookingUseCase;
        this.calculatePriceUseCase = calculatePriceUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto reserve(@RequestBody @Valid BookingRequestDto req) {
        var command = new IReserveRoomUseCase.Command(
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

    @GetMapping("/price-estimate")
    public PriceEstimateResponseDto estimate(
            @RequestParam Long roomTypeId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(defaultValue = "1") int quantity) {
        var command = new ICalculatePriceUseCase.Command(roomTypeId, from, to, quantity);
        return PriceEstimateResponseDto.from(calculatePriceUseCase.execute(command));
    }
}