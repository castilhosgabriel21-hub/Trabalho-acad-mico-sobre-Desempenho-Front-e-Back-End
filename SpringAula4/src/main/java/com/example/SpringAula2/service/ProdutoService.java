package com.example.SpringAula2.service;

import com.example.SpringAula2.model.Categoria;
import com.example.SpringAula2.model.Fornecedor;
import com.example.SpringAula2.model.Produto;
import com.example.SpringAula2.repository.CategoriaRepository;
import com.example.SpringAula2.repository.FornecedorRepository;
import com.example.SpringAula2.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    // 💾 SALVAR
    public Produto salvar(Produto produto) {

        if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(produto.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            produto.setCategoria(categoria);
        } else {
            produto.setCategoria(null);
        }

        if (produto.getFornecedores() != null && !produto.getFornecedores().isEmpty()) {
            List<Fornecedor> fornecedores = produto.getFornecedores().stream()
                    .map(f -> fornecedorRepository.findById(f.getId())
                            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado")))
                    .collect(Collectors.toList());
            produto.setFornecedores(fornecedores);
        } else {
            produto.setFornecedores(null);
        }

        return produtoRepository.save(produto);
    }

    // ✏️ ATUALIZAR
    public Produto atualizar(Long id, Produto novoProduto) {

        return produtoRepository.findById(id).map(produto -> {

            produto.setNome(novoProduto.getNome());
            produto.setPreco(novoProduto.getPreco());

            if (novoProduto.getCategoria() != null && novoProduto.getCategoria().getId() != null) {
                Categoria categoria = categoriaRepository.findById(novoProduto.getCategoria().getId())
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
                produto.setCategoria(categoria);
            } else {
                produto.setCategoria(null);
            }

            if (novoProduto.getFornecedores() != null && !novoProduto.getFornecedores().isEmpty()) {
                List<Fornecedor> fornecedores = novoProduto.getFornecedores().stream()
                        .map(f -> fornecedorRepository.findById(f.getId())
                                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado")))
                        .collect(Collectors.toList());
                produto.setFornecedores(fornecedores);
            } else {
                produto.setFornecedores(null);
            }

            return produtoRepository.save(produto);

        }).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    // 📑 PAGINAÇÃO (EXERCÍCIO PRINCIPAL)
    public Page<Produto> listarPaginado(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    // 📄 LISTAR TODOS (USADO NO PDF)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll(Sort.by("id").ascending());
    }

    // 🔍 BUSCAR POR ID
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    // ❌ DELETAR
    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }
}