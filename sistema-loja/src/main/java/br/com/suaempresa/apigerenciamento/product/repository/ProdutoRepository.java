package br.com.suaempresa.apigerenciamento.product.repository;

import br.com.suaempresa.apigerenciamento.product.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    long countByFornecedorId(Long fornecedorId);

    List<Produto> findByFornecedorIdAndDeletedTrue(Long fornecedorId);
}