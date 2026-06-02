package com.example.SpringAula2.controller;

import com.example.SpringAula2.model.Fornecedor;
import com.example.SpringAula2.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("fornecedores", fornecedorService.listarTodos());
        return "fornecedores/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("fornecedor", new Fornecedor());
        return "fornecedores/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Fornecedor fornecedor) {
        if (fornecedor.getId() != null) {
            fornecedorService.atualizar(fornecedor.getId(), fornecedor);
        } else {
            fornecedorService.salvar(fornecedor);
        }
        return "redirect:/fornecedores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Fornecedor fornecedor = fornecedorService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Fornecedor inválido: " + id));
        model.addAttribute("fornecedor", fornecedor);
        return "fornecedores/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        fornecedorService.deletar(id);
        return "redirect:/fornecedores";
    }
}

