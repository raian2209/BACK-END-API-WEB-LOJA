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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    // com a devida lógica de autorização (usuário vê os seus, admin vê todos).
}