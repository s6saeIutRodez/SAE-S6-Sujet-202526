// infrastructure/api/DashboardController.java
package fr.iut.rodez.hotel.domain.api;

import fr.iut.rodez.hotel.application.usecase.GetDashboardUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final GetDashboardUseCase getDashboardUseCase;

    public DashboardController(GetDashboardUseCase getDashboardUseCase) {
        this.getDashboardUseCase = getDashboardUseCase;
    }

    @GetMapping
    public GetDashboardUseCase.Result getDashboard() {
        return getDashboardUseCase.execute();
    }
}