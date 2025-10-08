package br.com.suaempresa.apigerenciamento.order.service;
import br.com.suaempresa.apigerenciamento.coupon.model.Cupom;
import br.com.suaempresa.apigerenciamento.coupon.model.TipoDesconto;
import br.com.suaempresa.apigerenciamento.coupon.repository.CupomRepository;
import br.com.suaempresa.apigerenciamento.exception.CupomInvalidoException;
import br.com.suaempresa.apigerenciamento.exception.CupomNotFoundException;
import br.com.suaempresa.apigerenciamento.exception.GlobalExceptionHandler;
import br.com.suaempresa.apigerenciamento.exception.ProdutoNotFoundException;
import br.com.suaempresa.apigerenciamento.order.dto.PedidoRequestDTO;
import br.com.suaempresa.apigerenciamento.order.dto.PedidoResponseDTO;
import br.com.suaempresa.apigerenciamento.order.model.ItemPedido;
import br.com.suaempresa.apigerenciamento.order.model.Pedido;
import br.com.suaempresa.apigerenciamento.order.model.StatusPedido;
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

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository, CupomRepository cupomRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.cupomRepository = cupomRepository;
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
            itemPedido.setPrecoUnitario(produto.getPreco()); // Congela o preço do produto no momento da compra

            itensPedido.add(itemPedido);

            // Calcula o subtotal do item e adiciona ao total do pedido
            totalPedido = totalPedido.add(
                    BigDecimal.valueOf(itemPedido.getPrecoUnitario()).multiply(BigDecimal.valueOf(itemPedido.getQuantidade()))
            );
        }

        pedido.setItens(itensPedido);

         if (requestDTO.getCodigoCupom() != null && !requestDTO.getCodigoCupom().isBlank()) {
             Cupom cupom = validarEObterCupom(requestDTO.getCodigoCupom());
             totalPedido = aplicarDesconto(totalPedido, cupom);
             pedido.setCupom(cupom);
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
            BigDecimal percentual = cupom.getValorDesconto().divide(new BigDecimal("100"));
            BigDecimal desconto = total.multiply(percentual);
            return total.subtract(desconto).setScale(2, RoundingMode.HALF_UP);
        } else if (cupom.getTipoDesconto() == TipoDesconto.FIXO) {
            return total.subtract(cupom.getValorDesconto()).max(BigDecimal.ZERO);
        }
        return total;
    }

    // Implementar métodos para buscar e listar pedidos, com verificação de permissão
    // ...

    public List<PedidoResponseDTO> findOrderByUser(User currentUser) {
        return Collections.singletonList(mapToResponseDTO((Pedido) pedidoRepository.findByClienteId(currentUser.getId())));
    }

    public PedidoResponseDTO findOrderById(Long id, User cliente) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new CupomNotFoundException("Pedido não encontrado com ID: " + id));

        if (!pedido.getCliente().equals(cliente)) {
            throw new AccessDeniedException("Acesso negado. Você não é o proprietário deste produto.");
        }

        return mapToResponseDTO(pedido);
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
        return response;
    }


    public PedidoResponseDTO cancelOrder(Long id, User currentUser) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new CupomNotFoundException("Pedido não encontrado com ID: " + id));

        if (!pedido.getCliente().equals(currentUser)) {
            throw new AccessDeniedException("Acesso negado. Você não é o proprietário deste produto.");
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
        return mapToResponseDTO(pedido);
    }
}