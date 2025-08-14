package br.com.suaempresa.apigerenciamento.order.dto;

import br.com.suaempresa.apigerenciamento.order.model.StatusPedido;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoResponseDTO {
    private Long id;
    private Long clienteId;
    private String clienteNome;
    private LocalDateTime dataPedido;
    private StatusPedido status;
    private Double total;
    private List<ItemPedidoResponseDTO> itens;
    private String codigoCupomAplicado;

    @Getter @Setter
    public static class ItemPedidoResponseDTO {
        private Long produtoId;
        private String produtoNome;
        private Integer quantidade;
        private Double precoUnitario;
    }
}