package br.com.suaempresa.apigerenciamento.product.service;

import br.com.suaempresa.apigerenciamento.exception.ProdutoNotFoundException;
import br.com.suaempresa.apigerenciamento.product.dto.ProdutoRequestDTO;
import br.com.suaempresa.apigerenciamento.product.dto.ProdutoResponseDTO;
import br.com.suaempresa.apigerenciamento.product.model.Produto;
import br.com.suaempresa.apigerenciamento.product.repository.ProdutoRepository;
import br.com.suaempresa.apigerenciamento.user.model.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public ProdutoResponseDTO createProduto(ProdutoRequestDTO requestDTO, User fornecedor) {
        Produto produto = new Produto();
        produto.setNome(requestDTO.getNome());
        produto.setDescricao(requestDTO.getDescricao());
        produto.setPreco(requestDTO.getPreco());
        produto.setFornecedor(fornecedor); // Associa o produto ao fornecedor logado

        Produto savedProduto = produtoRepository.save(produto);
        return mapToResponseDTO(savedProduto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> findAllProdutos() {
        return produtoRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO findProdutoById(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado com o ID: " + id));
        return mapToResponseDTO(produto);
    }

    @Transactional
    public ProdutoResponseDTO updateProduto(Long id, ProdutoRequestDTO requestDTO, User currentUser) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado com o ID: " + id));

        // VERIFICAÇÃO DE PERMISSÃO: Garante que o usuário logado é o dono do produto
        if (!produto.getFornecedor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Acesso negado. Você não é o proprietário deste produto.");
        }

        produto.setNome(requestDTO.getNome());
        produto.setDescricao(requestDTO.getDescricao());
        produto.setPreco(requestDTO.getPreco());

        Produto updatedProduto = produtoRepository.save(produto);
        return mapToResponseDTO(updatedProduto);
    }

    @Transactional
    public void deleteProduto(Long id, User currentUser) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado com o ID: " + id));

        // VERIFICAÇÃO DE PERMISSÃO
        if (!produto.getFornecedor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Acesso negado. Você não é o proprietário deste produto.");
        }

        // A anotação @SQLDelete no model fará a mágica do Soft Delete
        produtoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> findProductByIdFornecedor(User fornecedor){
        return produtoRepository.findByFornecedorId(fornecedor.getId()).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Método utilitário para mapear Entidade -> DTO
    private ProdutoResponseDTO mapToResponseDTO(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());

        ProdutoResponseDTO.FornecedorDTO fornecedorDTO = new ProdutoResponseDTO.FornecedorDTO();
        fornecedorDTO.setId(produto.getFornecedor().getId());
        fornecedorDTO.setNome(produto.getFornecedor().getNome());
        dto.setFornecedor(fornecedorDTO);

        return dto;
    }
}