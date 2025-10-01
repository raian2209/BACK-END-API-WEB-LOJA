package br.com.suaempresa.apigerenciamento.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoRequestDTO {

    @NotBlank(message = "O nome do produto não pode ser vazio.")
    private String nome;

    private String descricao;

    @NotNull(message = "se deve ter um valor")
    @Positive(message = "O preço deve ser um valor positivo.")
    private Double preco;
}
