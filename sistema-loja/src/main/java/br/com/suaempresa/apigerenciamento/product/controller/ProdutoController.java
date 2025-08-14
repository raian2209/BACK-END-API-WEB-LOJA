package br.com.suaempresa.apigerenciamento.product.controller;

import br.com.suaempresa.apigerenciamento.product.dto.ProdutoRequestDTO;
import br.com.suaempresa.apigerenciamento.product.dto.ProdutoResponseDTO;
import br.com.suaempresa.apigerenciamento.product.service.ProdutoService;
import br.com.suaempresa.apigerenciamento.user.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Endpoint PÚBLICO para listar todos os produtos ativos
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> getAllProdutos() {
        return ResponseEntity.ok(produtoService.findAllProdutos());
    }

    // Endpoint PÚBLICO para buscar um produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> getProdutoById(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.findProdutoById(id));
    }

    // Endpoint PROTEGIDO para um FORNECEDOR criar um novo produto
    @PostMapping
    @PreAuthorize("hasRole('FORNECEDOR')")
    public ResponseEntity<ProdutoResponseDTO> createProduto(
            @Valid @RequestBody ProdutoRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser) { // Injeta o usuário logado

        ProdutoResponseDTO responseDTO = produtoService.createProduto(requestDTO, currentUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Endpoint PROTEGIDO para um FORNECEDOR atualizar seu próprio produto
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FORNECEDOR')")
    public ResponseEntity<ProdutoResponseDTO> updateProduto(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser) {

        ProdutoResponseDTO responseDTO = produtoService.updateProduto(id, requestDTO, currentUser);
        return ResponseEntity.ok(responseDTO);
    }

    // Endpoint PROTEGIDO para um FORNECEDOR deletar (soft delete) seu próprio produto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FORNECEDOR')")
    public ResponseEntity<Void> deleteProduto(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        produtoService.deleteProduto(id, currentUser);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}