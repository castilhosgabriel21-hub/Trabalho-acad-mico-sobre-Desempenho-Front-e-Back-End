package com.example.SpringAula2.controller;

import com.example.SpringAula2.model.Produto;
import com.example.SpringAula2.service.CategoriaService;
import com.example.SpringAula2.service.FornecedorService;
import com.example.SpringAula2.service.ProdutoPdfService;
import com.example.SpringAula2.service.ProdutoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProdutoPdfService produtoPdfService;

    // ✅ PAGINAÇÃO CORRIGIDA
    @GetMapping
    public String listarProdutos(@RequestParam(defaultValue = "0") int page, Model model) {

        // evita página negativa
        if (page < 0) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, 20, Sort.by("id").ascending());
        Page<Produto> pagina = produtoService.listarPaginado(pageable);

        // evita página maior que o total (isso causava o erro 500)
        if (pagina.getTotalPages() > 0 && page >= pagina.getTotalPages()) {
            page = pagina.getTotalPages() - 1;
            pageable = PageRequest.of(page, 20, Sort.by("id").ascending());
            pagina = produtoService.listarPaginado(pageable);
        }

        model.addAttribute("pagina", pagina);
        model.addAttribute("produtos", pagina.getContent());

        return "produtos/lista";
    }

    @GetMapping("/novo")
    public String novoProduto(Model model) {
        Produto produto = new Produto();
        produto.setFornecedores(new ArrayList<>());

        model.addAttribute("produto", produto);
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("fornecedores", fornecedorService.listarTodos());

        return "produtos/form";
    }

    @GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable Long id, Model model) {

        Produto produto = produtoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        model.addAttribute("produto", produto);
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("fornecedores", fornecedorService.listarTodos());

        return "produtos/form";
    }

    @PostMapping("/salvar")
    public String salvarProduto(@ModelAttribute Produto produto) {

        if (produto.getId() != null) {
            produtoService.atualizar(produto.getId(), produto);
        } else {
            produtoService.salvar(produto);
        }

        return "redirect:/produtos";
    }

    @GetMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable Long id) {
        produtoService.deletar(id);
        return "redirect:/produtos";
    }

    @GetMapping("/pdf")
    public void gerarPdf(HttpServletResponse response) throws Exception {
        produtoPdfService.gerarPdf(response);
    }

    @GetMapping("/json")
    @ResponseBody
    public List<Produto> getProdutosJson() {
        return produtoService
                .listarPaginado(PageRequest.of(0, 10000, Sort.by("id").ascending()))
                .getContent();
    }
}