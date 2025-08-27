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
    @GetMapping
    public ResponseEntity<List<CupomResponseDTO>> findAllCupom() {
        return ResponseEntity.ok(cupomService.listCupom());
    }

    //TODO GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CupomResponseDTO> findCupomById(@PathVariable Long id) {
        return ResponseEntity.ok(cupomService.findCupom(id));
    }

    // TODO PUT
    @PutMapping("/{id}")
    public ResponseEntity<CupomResponseDTO> updateCupom(@PathVariable("id") Long id, @Valid @RequestBody CupomRequestDTO requestDTO) {
        CupomResponseDTO responseDTO = cupomService.updateCupom(id, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


    // TODO DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCupom(@PathVariable Long id) {
        cupomService.deleteCupom(id);
        return ResponseEntity.noContent().build();
    }
}