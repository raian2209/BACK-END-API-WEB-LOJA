package br.com.suaempresa.apigerenciamento.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoRequestDTO {
    @NotNull
    private Long produtoId;

    @NotNull
    @Positive(message = "A quantidade deve ser positiva.")
    private Integer quantidade;
}