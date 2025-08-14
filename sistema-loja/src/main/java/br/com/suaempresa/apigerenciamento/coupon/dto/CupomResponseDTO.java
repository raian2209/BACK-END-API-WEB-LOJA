package br.com.suaempresa.apigerenciamento.coupon.dto;

import br.com.suaempresa.apigerenciamento.coupon.model.TipoDesconto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CupomResponseDTO {
    private Long id;
    private String codigo;
    private TipoDesconto tipoDesconto;
    private BigDecimal valorDesconto;
    private LocalDate dataValidade;
    private boolean ativo;
}