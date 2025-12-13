package br.com.suaempresa.apigerenciamento.dashboard.controller;

import br.com.suaempresa.apigerenciamento.dashboard.dto.DashboardResumoDTO;
import br.com.suaempresa.apigerenciamento.dashboard.service.DashboardService;
import br.com.suaempresa.apigerenciamento.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {


    private final DashboardService dashboardService;

    public  DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/resumo")
    @PreAuthorize("hasRole('FORNECEDOR')")
    public ResponseEntity<DashboardResumoDTO> getResumo(@AuthenticationPrincipal User currentUser) {
        DashboardResumoDTO resumo = dashboardService.gerarResumoFornecedor(currentUser);
        return ResponseEntity.ok(resumo);
    }
}
