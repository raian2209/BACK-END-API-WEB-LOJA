package br.com.suaempresa.apigerenciamento.order.dto;

import br.com.suaempresa.apigerenciamento.order.model.StatusPedido;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class VendaFornecedorDTO {
    private Long pedidoId;
    private LocalDateTime dataVenda;
    private String nomeProduto;
    private Integer quantidade;
    private Double valorUnitario;
    private StatusPedido statusPedido;
    private String clienteNome;
    private Double subtotal;
}