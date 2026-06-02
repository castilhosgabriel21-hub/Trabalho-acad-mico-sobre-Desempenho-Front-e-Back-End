package com.example.SpringAula2.repository;

import com.example.SpringAula2.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
