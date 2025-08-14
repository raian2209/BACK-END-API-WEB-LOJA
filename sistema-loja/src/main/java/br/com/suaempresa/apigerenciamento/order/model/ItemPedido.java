package br.com.suaempresa.apigerenciamento.order.model;

import br.com.suaempresa.apigerenciamento.product.model.Produto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "itens_pedido")
@Getter
@Setter
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private Double precoUnitario; // Preço do produto no momento da compra
}