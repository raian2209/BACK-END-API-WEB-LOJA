package br.com.suaempresa.apigerenciamento.dashboard.service;

import br.com.suaempresa.apigerenciamento.dashboard.dto.DashboardResumoDTO;
import br.com.suaempresa.apigerenciamento.order.repository.ItemPedidoRepository;
import br.com.suaempresa.apigerenciamento.product.repository.ProdutoRepository;
import br.com.suaempresa.apigerenciamento.user.model.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {


    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoRepository produtoRepository;

    public DashboardService(ItemPedidoRepository itemPedidoRepository, ProdutoRepository produtoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResumoDTO gerarResumoFornecedor(User fornecedor) {
        Long fornecedorId = fornecedor.getId();
        Long totalProdutos = produtoRepository.countByFornecedorId(fornecedorId);
        Long totalVendas = itemPedidoRepository.countVendasByFornecedor(fornecedorId);
        Double faturamento = itemPedidoRepository.calcularFaturamentoTotal(fornecedorId);
        if (faturamento == null) faturamento = 0.0;
        return new DashboardResumoDTO(totalProdutos, totalVendas, faturamento);
    }
}