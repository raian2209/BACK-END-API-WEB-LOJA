package br.com.suaempresa.apigerenciamento.order.model;

import br.com.suaempresa.apigerenciamento.coupon.model.Cupom;
import br.com.suaempresa.apigerenciamento.user.model.User;
// import br.com.suaempresa.apigerenciamento.coupon.model.Cupom; // Se tivesse o módulo de cupom
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private User cliente;

    @Column(nullable = false)
    private LocalDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Column(nullable = false)
    private Double total;

    // A mágica da cascata: ao salvar um Pedido, os ItensPedido associados também serão salvos.
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();


    // Relacionamento opcional com Cupom
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cupom_id")
    private Cupom cupom;


    @PrePersist
    public void onPrePersist() {
        dataPedido = LocalDateTime.now();
    }
}