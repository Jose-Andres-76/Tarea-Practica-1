package com.project.demo.logic.entity.categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    @Query("SELECT u FROM Categoria u WHERE u.nombre = ?1")
    Optional<Categoria> findByNombre(String nombre);
}
