package br.com.suaempresa.apigerenciamento.coupon.controller;

import br.com.suaempresa.apigerenciamento.coupon.dto.CupomRequestDTO;
import br.com.suaempresa.apigerenciamento.coupon.dto.CupomResponseDTO;
import br.com.suaempresa.apigerenciamento.coupon.service.CupomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cupons")
@PreAuthorize("hasRole('ADMIN')") // Protege todos os métodos da classe
public class CupomController {

    private final CupomService cupomService;

    public CupomController(CupomService cupomService) {
        this.cupomService = cupomService;
    }

    @PostMapping
    public ResponseEntity<CupomResponseDTO> createCupom(@Valid @RequestBody CupomRequestDTO requestDTO) {
        CupomResponseDTO responseDTO = cupomService.createCupom(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // TODO GET

    // TODO PUT

    // TODO DELETE
}