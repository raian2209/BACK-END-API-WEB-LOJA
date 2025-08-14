package br.com.suaempresa.apigerenciamento.coupon.repository;

import br.com.suaempresa.apigerenciamento.coupon.model.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {
    Optional<Cupom> findByCodigo(String codigo);
}