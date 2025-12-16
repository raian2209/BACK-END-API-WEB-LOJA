package br.com.suaempresa.apigerenciamento.order.repository;

import br.com.suaempresa.apigerenciamento.order.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {


    // QUERY CUSTOMIZADA:
    // Seleciona o ItemPedido (i)
    // Faz JOIN com o Produto (p) para verificar o fornecedor
    // Faz JOIN FETCH com Pedido para já trazer a data e evitar queries extras
    @Query("SELECT i FROM ItemPedido i " +
            "JOIN FETCH i.pedido ped " +
            "JOIN FETCH i.produto prod " +
            "WHERE prod.fornecedor.id = :fornecedorId " +
            "AND ped.status <> 'CANCELADO' " +
            "ORDER BY ped.dataPedido DESC")
    List<ItemPedido> findAllVendasByFornecedor(@Param("fornecedorId") Long fornecedorId);


    @Query("SELECT COUNT(i) FROM ItemPedido i WHERE i.produto.fornecedor.id = :id AND i.pedido.status <> 'CANCELADO'")
    long countVendasByFornecedor(@Param("id") Long id);

    @Query("""
    SELECT COALESCE(SUM(p.total), 0)
    FROM Pedido p
    WHERE p.status <> 'CANCELADO'
    AND EXISTS (
        SELECT 1
        FROM ItemPedido i
        WHERE i.pedido = p
        AND i.produto.fornecedor.id = :fornecedorId
    )
    """)
    Double calcularFaturamentoTotal(@Param("fornecedorId") Long fornecedorId);
}