package br.com.suaempresa.apigerenciamento.order.repository;

import br.com.suaempresa.apigerenciamento.order.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Para um usuário buscar seus próprios pedidos
    List<Pedido> findByClienteId(Long clienteId);
}