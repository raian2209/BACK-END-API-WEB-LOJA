package br.com.suaempresa.apigerenciamento.coupon.dto;

import br.com.suaempresa.apigerenciamento.coupon.model.TipoDesconto;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CupomRequestDTO {
    @NotBlank(message = "O código não pode ser vazio.")
    private String codigo;

    @NotNull(message = "O tipo de desconto é obrigatório.")
    private TipoDesconto tipoDesconto;

    @NotNull(message = "O valor do desconto é obrigatório.")
    @Positive(message = "O valor do desconto deve ser positivo.")
    private BigDecimal valorDesconto;

    @NotNull(message = "A data de validade é obrigatória.")
    @Future(message = "A data de validade deve ser no futuro.")
    private LocalDate dataValidade;
}