package br.com.suaempresa.apigerenciamento.order.service;
import br.com.suaempresa.apigerenciamento.coupon.model.Cupom;
import br.com.suaempresa.apigerenciamento.coupon.model.TipoDesconto;
import br.com.suaempresa.apigerenciamento.coupon.repository.CupomRepository;
import br.com.suaempresa.apigerenciamento.exception.*;
import br.com.suaempresa.apigerenciamento.order.dto.PedidoRequestDTO;
import br.com.suaempresa.apigerenciamento.order.dto.PedidoResponseDTO;
import br.com.suaempresa.apigerenciamento.order.dto.VendaFornecedorDTO;
import br.com.suaempresa.apigerenciamento.order.model.ItemPedido;
import br.com.suaempresa.apigerenciamento.order.model.Pedido;
import br.com.suaempresa.apigerenciamento.order.model.StatusPedido;
import br.com.suaempresa.apigerenciamento.order.repository.ItemPedidoRepository;
import br.com.suaempresa.apigerenciamento.order.repository.PedidoRepository;
import br.com.suaempresa.apigerenciamento.product.model.Produto;
import br.com.suaempresa.apigerenciamento.product.repository.ProdutoRepository;
import br.com.suaempresa.apigerenciamento.user.model.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final CupomRepository cupomRepository; // Injetaria se tivesse
    private final ItemPedidoRepository itemPedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository,
                         CupomRepository cupomRepository,  ItemPedidoRepository itemPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.cupomRepository = cupomRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Transactional
    public PedidoResponseDTO createPedido(PedidoRequestDTO requestDTO, User cliente) {
        // 1. Cria a entidade Pedido e associa ao cliente
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.PROCESSANDO);

        BigDecimal totalPedido = BigDecimal.ZERO;
        List<ItemPedido> itensPedido = new ArrayList<>();

        // 2. Processa cada item do pedido
        for (var itemDTO : requestDTO.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado com ID: " + itemDTO.getProdutoId()));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemDTO.getQuantidade());

            BigDecimal precoUnitario = BigDecimal.valueOf(produto.getPreco());
            itemPedido.setPrecoUnitario(produto.getPreco());

            itensPedido.add(itemPedido);

            totalPedido = totalPedido.add(
                    precoUnitario.multiply(BigDecimal.valueOf(itemPedido.getQuantidade()))
            );
        }

        pedido.setItens(itensPedido);

        if (requestDTO.getCodigoCupom() != null && !requestDTO.getCodigoCupom().isBlank()) {
            Cupom cupom = validarEObterCupom(requestDTO.getCodigoCupom());
            totalPedido = aplicarDesconto(totalPedido, cupom);
            pedido.setCupom(cupom);
            System.out.println(totalPedido);
        }

        if (totalPedido.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidZeroOrNegativeException("Valor não pode ser igual a zero");
        }

        pedido.setTotal(totalPedido.doubleValue());

        Pedido savedPedido = pedidoRepository.save(pedido);

        return mapToResponseDTO(savedPedido);
    }

    private Cupom validarEObterCupom(String codigo) {
        Cupom cupom = cupomRepository.findByCodigo(codigo.toUpperCase())
                .orElseThrow(() -> new CupomNotFoundException("Cupom com o código '" + codigo + "' não encontrado."));

        if (!cupom.isAtivo()) {
            throw new CupomInvalidoException("O cupom '" + codigo + "' não está mais ativo.");
        }

        if (cupom.getDataValidade().isBefore(LocalDate.now())) {
            throw new CupomInvalidoException("O cupom '" + codigo + "' expirou em " + cupom.getDataValidade() + ".");
        }

        return cupom;
    }

    private BigDecimal aplicarDesconto(BigDecimal total, Cupom cupom) {
        if (cupom.getTipoDesconto() == TipoDesconto.PERCENTAGEM) {
            BigDecimal desconto = total.multiply(cupom.getValorDesconto().divide(BigDecimal.valueOf(100)));
            return total.subtract(desconto);
        } else {
            return total.subtract(cupom.getValorDesconto());
        }
    }

    // Implementar métodos para buscar e listar pedidos, com verificação de permissão
    // ...

    public List<PedidoResponseDTO> findOrderByUser(User currentUser) {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(currentUser.getId());
        return pedidos.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public PedidoResponseDTO findOrderById(Long id, User cliente) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new CupomNotFoundException("Pedido não encontrado com ID: " + id));

        if (!pedido.getCliente().equals(cliente)) {
            throw new AccessDeniedException("Acesso negado. Você não é o proprietário deste produto.");
        }

        return mapToResponseDTO(pedido);
    }

    @Transactional(readOnly = true)
    public List<VendaFornecedorDTO> listarVendasDoFornecedor(User fornecedor) {

        List<ItemPedido> itensVendidos =
                itemPedidoRepository.findAllVendasByFornecedor(fornecedor.getId());

        return itensVendidos.stream().map(item -> {

            Pedido pedido = item.getPedido();

            VendaFornecedorDTO dto = new VendaFornecedorDTO();
            dto.setPedidoId(pedido.getId());
            dto.setDataVenda(pedido.getDataPedido());
            dto.setNomeProduto(item.getProduto().getNome());
            dto.setQuantidade(item.getQuantidade());
            dto.setValorUnitario(item.getPrecoUnitario());
            dto.setStatusPedido(pedido.getStatus());
            dto.setClienteNome(pedido.getCliente().getNome());

            double subtotalItem = item.getPrecoUnitario() * item.getQuantidade();

            double totalBrutoPedido = pedido.getItens().stream()
                    .mapToDouble(i -> i.getPrecoUnitario() * i.getQuantidade())
                    .sum();

            double fatorDesconto = pedido.getTotal() / totalBrutoPedido;
            double subtotalComDesconto = subtotalItem * fatorDesconto;


            dto.setSubtotal(subtotalComDesconto);

            return dto;

        }).toList();
    }


    private PedidoResponseDTO mapToResponseDTO(Pedido pedido) {
        PedidoResponseDTO response = new PedidoResponseDTO();
        response.setId(pedido.getId());
        response.setClienteId(pedido.getCliente().getId());
        response.setClienteNome(pedido.getCliente().getNome());
        response.setDataPedido(pedido.getDataPedido());
        response.setStatus(pedido.getStatus());
        response.setTotal(pedido.getTotal());

        List<PedidoResponseDTO.ItemPedidoResponseDTO> itensDTO = pedido.getItens().stream().map(item -> {
            var itemDTO = new PedidoResponseDTO.ItemPedidoResponseDTO();
            itemDTO.setProdutoId(item.getProduto().getId());
            itemDTO.setProdutoNome(item.getProduto().getNome());
            itemDTO.setQuantidade(item.getQuantidade());
            itemDTO.setPrecoUnitario(item.getPrecoUnitario());
            return itemDTO;
        }).toList();

        response.setItens(itensDTO);
        if (pedido.getCupom() != null) {
            response.setCodigoCupomAplicado(pedido.getCupom().getCodigo());
        }
        return response;
    }


    public PedidoResponseDTO cancelOrder(Long id, User currentUser) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new CupomNotFoundException("Pedido não encontrado com ID: " + id));

        if (!pedido.getCliente().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Acesso negado. Você não é o proprietário deste produto.");
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
        return mapToResponseDTO(pedido);
    }
}