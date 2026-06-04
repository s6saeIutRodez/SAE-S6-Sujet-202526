package fr.iut.rodez.hotel.infrastructure.api;

import fr.iut.rodez.hotel.application.usecase.AddPriceToRoomTypeUseCase;
import fr.iut.rodez.hotel.application.usecase.CreateRoomTypeUseCase;
import fr.iut.rodez.hotel.application.usecase.GetRoomTypeUseCase;
import fr.iut.rodez.hotel.domain.port.in.ICreateRoomTypeUseCase;
import fr.iut.rodez.hotel.infrastructure.api.dto.RoomTypeDetailResponseDto;
import fr.iut.rodez.hotel.infrastructure.api.dto.RoomTypePriceRequestDto;
import fr.iut.rodez.hotel.infrastructure.api.dto.RoomTypeRequestDto;
import fr.iut.rodez.hotel.infrastructure.api.dto.RoomTypeResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * US-01 — Créer un type de chambre / Consulter les types de chambres
 * US-02 — Définir un tarif par période
 */
@RestController
@RequestMapping("/room-types")
public class RoomTypeController {

    private final CreateRoomTypeUseCase createRoomTypeUseCase;
    private final GetRoomTypeUseCase getRoomTypeUseCase;
    private final AddPriceToRoomTypeUseCase addPriceToRoomTypeUseCase;

    public RoomTypeController(CreateRoomTypeUseCase createRoomTypeUseCase,
                              GetRoomTypeUseCase getRoomTypeUseCase,
                              AddPriceToRoomTypeUseCase addPriceToRoomTypeUseCase) {
        this.createRoomTypeUseCase = createRoomTypeUseCase;
        this.getRoomTypeUseCase = getRoomTypeUseCase;
        this.addPriceToRoomTypeUseCase = addPriceToRoomTypeUseCase;
    }

    /** US-01 — Lister tous les types de chambres avec leurs tarifs */
    @GetMapping
    public List<RoomTypeDetailResponseDto> findAll() {
        return getRoomTypeUseCase.executeAll().stream()
                .map(RoomTypeDetailResponseDto::from)
                .toList();
    }

    /** US-01 — Consulter un type de chambre par identifiant */
    @GetMapping("/{id}")
    public RoomTypeDetailResponseDto findById(@PathVariable Long id) {
        return RoomTypeDetailResponseDto.from(getRoomTypeUseCase.execute(id));
    }

    /** US-01 — Créer un nouveau type de chambre */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomTypeResponse create(@RequestBody @Valid RoomTypeRequestDto req) {
        ICreateRoomTypeUseCase.Command command =
                new ICreateRoomTypeUseCase.Command(req.name(), req.totalRooms());
        CreateRoomTypeUseCase.Result result = createRoomTypeUseCase.execute(command);

        return new RoomTypeResponse(result.id(), result.name(), result.totalRooms());
    }

    /** US-02 — Ajouter un tarif par période à un type de chambre */
    @PostMapping("/{id}/prices")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPrice(@PathVariable Long id,
                         @RequestBody @Valid RoomTypePriceRequestDto req) {
        addPriceToRoomTypeUseCase.execute(
                id, req.startDate(), req.endDate(), req.pricePerNight());
    }
}
