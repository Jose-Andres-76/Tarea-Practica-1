package com.project.demo.rest.categoria;

import com.project.demo.logic.entity.categoria.Categoria;
import com.project.demo.logic.entity.categoria.CategoriaRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoriaRestController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<?> getAllCategorias(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page -1, size);
        List<Categoria> categorias = categoriaRepository.findAll(pageable).getContent();
        int totalElements = (int) categoriaRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(totalPages);
        meta.setTotalElements(totalElements);
        meta.setPageNumber(page);
        meta.setPageSize(size);
        return new GlobalResponseHandler().handleResponse("Categorias listadas.", categorias, HttpStatus.OK, meta);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<?> getCategoriaById(@PathVariable Long id, HttpServletRequest request) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isPresent()) {
            return new GlobalResponseHandler().handleResponse(
                    "Categoria encontrada.", categoria.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Categoria con el id: " + id + " no encontrada.", HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> createCategoria(@RequestBody Categoria categoria, HttpServletRequest request) {
        Categoria savedCategoria = categoriaRepository.save(categoria);
        return new GlobalResponseHandler().handleResponse("Categoria creada.", savedCategoria, HttpStatus.CREATED, request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria, HttpServletRequest request) {
        Optional<Categoria> existingCategoriaOpt = categoriaRepository.findById(id);
        if (existingCategoriaOpt.isPresent()) {
            Categoria existingCategoria = existingCategoriaOpt.get();
            if (categoria.getNombre() != null) {
                existingCategoria.setNombre(categoria.getNombre());
            }
            if (categoria.getDescripcion() != null) {
                existingCategoria.setDescripcion(categoria.getDescripcion());
            }
            Categoria updatedCategoria = categoriaRepository.save(existingCategoria);
            return new GlobalResponseHandler().handleResponse("Categoria actualizada.", updatedCategoria, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Categoria con el id: " + id + " no encontrada.", HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteCategoria(@PathVariable Long id, HttpServletRequest request) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isPresent()) {
            categoriaRepository.deleteById(id);
            return new GlobalResponseHandler().handleResponse("Categoria eliminada.", categoria.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Categoria con el id: " + id + " no encontrada.", HttpStatus.NOT_FOUND, request);
        }
    }
}



