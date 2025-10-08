package br.com.suaempresa.apigerenciamento.coupon.model;

import br.com.suaempresa.apigerenciamento.order.model.Pedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cupons")
@Getter
@Setter
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDesconto tipoDesconto;

    @Column(nullable = false)
    private BigDecimal valorDesconto;

    @Column(nullable = false)
    private LocalDate dataValidade;

    @Column(nullable = false)
    private boolean ativo;

    @OneToMany(mappedBy = "cupom")
    private List<Pedido> pedidos;
}