package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.ConsultorDashboardResponse;
import br.com.munnincrow.api.dto.DashboardService;
import br.com.munnincrow.api.dto.EmpreendedorDashboardResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/empreendedor/{userId}")
    public ResponseEntity<EmpreendedorDashboardResponse> getDashboardEmpreendedor(
            @PathVariable Long userId
    ) {
        EmpreendedorDashboardResponse resp = dashboardService.montarDashboardEmpreendedor(userId);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/consultor/{userId}")
    public ResponseEntity<ConsultorDashboardResponse> getDashboardConsultor(
            @PathVariable Long userId
    ) {
        ConsultorDashboardResponse resp = dashboardService.montarDashboardConsultor(userId);
        return ResponseEntity.ok(resp);
    }
}