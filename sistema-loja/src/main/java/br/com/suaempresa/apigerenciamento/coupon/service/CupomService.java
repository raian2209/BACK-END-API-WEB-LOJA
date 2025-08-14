package br.com.suaempresa.apigerenciamento.coupon.service;
import br.com.suaempresa.apigerenciamento.coupon.dto.CupomRequestDTO;
import br.com.suaempresa.apigerenciamento.coupon.dto.CupomResponseDTO;
import br.com.suaempresa.apigerenciamento.coupon.model.Cupom;
import br.com.suaempresa.apigerenciamento.coupon.repository.CupomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CupomService {

    private final CupomRepository cupomRepository;

    public CupomService(CupomRepository cupomRepository) {
        this.cupomRepository = cupomRepository;
    }

    @Transactional
    public CupomResponseDTO createCupom(CupomRequestDTO requestDTO) {
        // Lógica para verificar se o código já existe...
        Cupom cupom = new Cupom();
        cupom.setCodigo(requestDTO.getCodigo().toUpperCase());
        cupom.setTipoDesconto(requestDTO.getTipoDesconto());
        cupom.setValorDesconto(requestDTO.getValorDesconto());
        cupom.setDataValidade(requestDTO.getDataValidade());
        cupom.setAtivo(true);

        Cupom savedCupom = cupomRepository.save(cupom);
        return mapToResponseDTO(savedCupom);
    }

    // TODO findAll


    // TODO findById

    // TODO update


    // TODO delete

    private CupomResponseDTO mapToResponseDTO(Cupom cupom) {
        // Lógica de mapeamento...
        return new CupomResponseDTO(); // Implementação omitida por brevidade
    }
}