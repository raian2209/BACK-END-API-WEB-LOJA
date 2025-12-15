package br.com.suaempresa.apigerenciamento.coupon.service;
import br.com.suaempresa.apigerenciamento.coupon.dto.CupomRequestDTO;
import br.com.suaempresa.apigerenciamento.coupon.dto.CupomResponseDTO;
import br.com.suaempresa.apigerenciamento.coupon.model.Cupom;
import br.com.suaempresa.apigerenciamento.coupon.repository.CupomRepository;
import br.com.suaempresa.apigerenciamento.exception.CupomNotFoundException;
import br.com.suaempresa.apigerenciamento.exception.ProdutoNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional(readOnly = true)
    public List<CupomResponseDTO> listCupom() {
        return cupomRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // TODO findById
    @Transactional(readOnly = true)
    public CupomResponseDTO findCupom(Long id) {
        Cupom cupom = cupomRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado com o ID: " + id));
        return mapToResponseDTO(cupom);
    }

    @Transactional(readOnly = true)
    public CupomResponseDTO findCupomByCodigo(String codigo) {
        Cupom cupom = cupomRepository.findByCodigo(codigo)
                .orElseThrow(() -> new CupomNotFoundException("Cupom não encontrado com o código: " + codigo));
        return mapToResponseDTO(cupom);
    }


    // TODO update
    @Transactional
    public CupomResponseDTO updateCupom(Long id,CupomRequestDTO requestDTO) {
        Cupom cupom = cupomRepository.findById(id)
                .orElseThrow(() -> new CupomNotFoundException("Cupom não encontrado com o ID: " + id));

        cupom.setTipoDesconto(requestDTO.getTipoDesconto());
        cupom.setValorDesconto(requestDTO.getValorDesconto());
        cupom.setDataValidade(requestDTO.getDataValidade());
        cupom.setAtivo(cupom.isAtivo());

        Cupom updateCupom = cupomRepository.save(cupom);

        return mapToResponseDTO(updateCupom);
    }

    // TODO delete
    @Transactional
    public void deleteCupom(Long id) {

        cupomRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado com o ID: " + id));

        cupomRepository.deleteById(id);
    }

    private CupomResponseDTO mapToResponseDTO(Cupom cupom) {
        CupomResponseDTO dto = new CupomResponseDTO();
        dto.setId(cupom.getId());
        dto.setCodigo(cupom.getCodigo().toUpperCase());
        dto.setTipoDesconto(cupom.getTipoDesconto());
        dto.setValorDesconto(cupom.getValorDesconto());
        dto.setDataValidade(cupom.getDataValidade());
        dto.setAtivo(cupom.isAtivo());
        return  dto;
    }
}