package com.example.SpringAula2.repository;

import com.example.SpringAula2.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
