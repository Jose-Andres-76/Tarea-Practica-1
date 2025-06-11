package com.project.demo.rest.producto;

import com.project.demo.logic.entity.producto.Producto;
import com.project.demo.logic.entity.producto.ProductoRepository;
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
@RequestMapping("/productos")
public class ProductoRestController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated() && hasAnyRole('ADMIN','USER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAllProductos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        List<Producto> productos = productoRepository.findAll(pageable).getContent();
        int totalElements = (int) productoRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(totalPages);
        meta.setTotalElements(totalElements);
        meta.setPageNumber(page);
        meta.setPageSize(size);
        return new GlobalResponseHandler().handleResponse("Productos listados.", productos, HttpStatus.OK, meta);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('ADMIN','USER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getProductoById(@PathVariable Long id, HttpServletRequest request) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Producto encontrado.", producto.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Producto con el id: " + id + " no encontrado.", HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping
    @PreAuthorize("isAuthenticated() && hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> createProducto(@RequestBody Producto producto, HttpServletRequest request) {
        Producto savedProducto = productoRepository.save(producto);
        return new GlobalResponseHandler().handleResponse("Producto creado exitosamente.", savedProducto, HttpStatus.CREATED, request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @RequestBody Producto producto, HttpServletRequest request) {
        Optional<Producto> existingProductoOpt = productoRepository.findById(id);
        if (existingProductoOpt.isPresent()) {
            Producto existingProducto = existingProductoOpt.get();

            if (producto.getNombre() != null) {
                existingProducto.setNombre(producto.getNombre());
            }
            if (producto.getDescripcion() != null) {
                existingProducto.setDescripcion(producto.getDescripcion());
            }
            if (producto.getPrecio() != 0) {
                existingProducto.setPrecio(producto.getPrecio());
            }
            if (producto.getStock() != 0) {
                existingProducto.setStock(producto.getStock());
            }
            if (producto.getCategoria() != null) {
                existingProducto.setCategoria(producto.getCategoria());
            }

            Producto updatedProducto = productoRepository.save(existingProducto);
            return new GlobalResponseHandler().handleResponse("Producto actualizado exitosamente.", updatedProducto, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Producto con el id: " + id + " no encontrado.", HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id, HttpServletRequest request) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            productoRepository.deleteById(id);
            return new GlobalResponseHandler().handleResponse("Producto eliminado exitosamente.", producto.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Producto con el id: " + id + " no encontrado.", HttpStatus.NOT_FOUND, request);
        }
    }
}
