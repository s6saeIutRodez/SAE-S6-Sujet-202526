package fr.iut.rodez.hotel.infrastructure.api;

import fr.iut.rodez.hotel.application.usecase.GetDashboardUseCase;
import fr.iut.rodez.hotel.infrastructure.api.dto.DashboardResponseDto;
import org.springframework.web.bind.annotation.*;

/**
 * US-07 — Consulter le tableau de bord
 * Retourne un DTO plutôt que le Result interne du use case.
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final GetDashboardUseCase getDashboardUseCase;

    public DashboardController(GetDashboardUseCase getDashboardUseCase) {
        this.getDashboardUseCase = getDashboardUseCase;
    }

    @GetMapping
    public DashboardResponseDto getDashboard() {
        return DashboardResponseDto.from(getDashboardUseCase.execute());
    }
}
