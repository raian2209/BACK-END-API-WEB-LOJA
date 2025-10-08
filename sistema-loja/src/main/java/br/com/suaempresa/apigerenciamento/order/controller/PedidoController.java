package br.com.suaempresa.apigerenciamento.order.controller;

import br.com.suaempresa.apigerenciamento.order.dto.PedidoRequestDTO;
import br.com.suaempresa.apigerenciamento.order.dto.PedidoResponseDTO;
import br.com.suaempresa.apigerenciamento.order.service.PedidoService;
import br.com.suaempresa.apigerenciamento.user.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Endpoint protegido para um USUARIO criar um novo pedido
    @PostMapping
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<PedidoResponseDTO> createPedido(
            @Valid @RequestBody PedidoRequestDTO requestDTO,
            @AuthenticationPrincipal User currentUser) { // Pega o usuário logado

        PedidoResponseDTO responseDTO = pedidoService.createPedido(requestDTO, currentUser);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Outros endpoints para listar e buscar pedidos seriam implementados aqui
    @GetMapping
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<List<PedidoResponseDTO>> getAllPedidos(
            @AuthenticationPrincipal User currentUser
    ){
            List<PedidoResponseDTO> listOrder = pedidoService.findOrderByUser(currentUser);
            return new ResponseEntity<>(listOrder, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<PedidoResponseDTO> getPedido(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ){
        PedidoResponseDTO pedidoResponseDTO = pedidoService.findOrderById(id, currentUser);
        return new ResponseEntity<>(pedidoResponseDTO, HttpStatus.OK);
    }

    // com a devida lógica de autorização (usuário vê os seus, admin vê todos).
}