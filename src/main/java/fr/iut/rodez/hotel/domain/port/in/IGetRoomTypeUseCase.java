package fr.iut.rodez.hotel.domain.port.in;

import fr.iut.rodez.hotel.application.usecase.GetRoomTypeUseCase;
import java.util.List;

/**
 * US-01 — Consulter les types de chambres
 */
public interface IGetRoomTypeUseCase {
    GetRoomTypeUseCase.Result execute(Long id);
    List<GetRoomTypeUseCase.Result> executeAll();
}
