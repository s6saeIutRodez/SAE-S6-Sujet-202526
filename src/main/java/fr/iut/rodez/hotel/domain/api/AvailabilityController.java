// infrastructure/api/AvailabilityController.java
package fr.iut.rodez.hotel.domain.api;

import fr.iut.rodez.hotel.application.usecase.CheckAvailabilityUseCase;
import fr.iut.rodez.hotel.domain.api.dto.AvailabilityResponseDto;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    private final CheckAvailabilityUseCase checkAvailabilityUseCase;

    public AvailabilityController(CheckAvailabilityUseCase checkAvailabilityUseCase) {
        this.checkAvailabilityUseCase = checkAvailabilityUseCase;
    }

    @GetMapping
    public AvailabilityResponseDto check(@RequestParam Long roomTypeId,
                                         @RequestParam LocalDate from,
                                         @RequestParam LocalDate to,
                                         @RequestParam(defaultValue = "1") int qty) {
        return AvailabilityResponseDto.from(
                checkAvailabilityUseCase.execute(roomTypeId, from, to, qty));
    }
}