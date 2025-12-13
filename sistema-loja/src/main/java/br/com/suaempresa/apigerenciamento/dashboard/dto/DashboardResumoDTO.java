package br.com.suaempresa.apigerenciamento.dashboard.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResumoDTO {
    private Long totalProdutos;
    private Long totalVendas;
    private Double faturamentoTotal;

}