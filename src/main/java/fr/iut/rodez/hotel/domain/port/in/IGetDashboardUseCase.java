package fr.iut.rodez.hotel.domain.port.in;

import fr.iut.rodez.hotel.application.usecase.GetDashboardUseCase;

/**
 * US-07 — Consulter le tableau de bord
 */
public interface IGetDashboardUseCase {
    GetDashboardUseCase.Result execute();
}
