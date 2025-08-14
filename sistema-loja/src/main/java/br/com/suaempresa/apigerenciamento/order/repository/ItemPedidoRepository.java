package br.com.suaempresa.apigerenciamento.order.repository;

import br.com.suaempresa.apigerenciamento.order.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {}