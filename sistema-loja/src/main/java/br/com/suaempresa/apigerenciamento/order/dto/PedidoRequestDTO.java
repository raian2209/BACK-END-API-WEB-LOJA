package br.com.suaempresa.apigerenciamento.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoRequestDTO {

    @NotEmpty(message = "A lista de itens não pode ser vazia.")
    @Valid // Valida os objetos dentro da lista
    private List<ItemPedidoRequestDTO> itens;

    private String codigoCupom; // Opcional
}