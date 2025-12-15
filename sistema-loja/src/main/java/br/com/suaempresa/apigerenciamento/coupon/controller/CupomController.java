package br.com.suaempresa.apigerenciamento.coupon.controller;

import br.com.suaempresa.apigerenciamento.coupon.dto.CupomRequestDTO;
import br.com.suaempresa.apigerenciamento.coupon.dto.CupomResponseDTO;
import br.com.suaempresa.apigerenciamento.coupon.service.CupomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cupons")
// REMOVIDO: @PreAuthorize("hasRole('ADMIN')") da classe para permitir controle granular
public class CupomController {

    private final CupomService cupomService;

    public CupomController(CupomService cupomService) {
        this.cupomService = cupomService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Apenas Admin cria
    public ResponseEntity<CupomResponseDTO> createCupom(@Valid @RequestBody CupomRequestDTO requestDTO) {
        CupomResponseDTO responseDTO = cupomService.createCupom(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CupomResponseDTO>> findAllCupom() {
        return ResponseEntity.ok(cupomService.listCupom());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CupomResponseDTO> findCupomById(@PathVariable Long id) {
        return ResponseEntity.ok(cupomService.findCupom(id));
    }


    @GetMapping("/codigo/{codigo}")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<CupomResponseDTO> findCupomByCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(cupomService.findCupomByCodigo(codigo));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas Admin atualiza
    public ResponseEntity<CupomResponseDTO> updateCupom(@PathVariable("id") Long id, @Valid @RequestBody CupomRequestDTO requestDTO) {
        CupomResponseDTO responseDTO = cupomService.updateCupom(id, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas Admin deleta
    public ResponseEntity<Void> deleteCupom(@PathVariable Long id) {
        cupomService.deleteCupom(id);
        return ResponseEntity.noContent().build();
    }
}