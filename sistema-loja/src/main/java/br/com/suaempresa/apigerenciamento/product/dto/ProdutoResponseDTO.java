package br.com.suaempresa.apigerenciamento.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private FornecedorDTO fornecedor; // DTO aninhado para dados do fornecedor

    // DTO interno para evitar expor a entidade User completa
    @Getter
    @Setter
    public static class FornecedorDTO {
        private Long id;
        private String nome;
    }
}