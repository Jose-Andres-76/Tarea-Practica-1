//package com.project.demo.logic.entity.producto;
//
//import com.project.demo.logic.entity.categoria.Categoria;
//import com.project.demo.logic.entity.categoria.CategoriaRepository;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class ProductoSeeder implements ApplicationListener<ContextRefreshedEvent> {
//
//    private final ProductoRepository productoRepository;
//    private final CategoriaRepository categoriaRepository;
//
//    public ProductoSeeder(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
//        this.productoRepository = productoRepository;
//        this.categoriaRepository = categoriaRepository;
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        this.createProductos();
//    }
//
//    private void createProductos() {
//        if (productoRepository.count() == 0) {
//            Optional<Categoria> electronicaOpt = categoriaRepository.findAll()
//                    .stream()
//                    .filter(c -> "Electrónica".equalsIgnoreCase(c.getNombre()))
//                    .findFirst();
//
//            if (electronicaOpt.isPresent()) {
//                Categoria electronica = electronicaOpt.get();
//
//                Producto producto1 = new Producto();
//                producto1.setNombre("Laptop");
//                producto1.setDescripcion("Laptop de alta gama.");
//                producto1.setPrecio(1500.00);
//                producto1.setStock(50);
//                producto1.setCategoria(electronica);
//                productoRepository.save(producto1);
//            }
//        }
//    }
//}

package com.project.demo.logic.entity.producto;

import com.project.demo.logic.entity.categoria.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Order(4)
@Component
public class ProductoSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoSeeder(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createProductos();
    }

    private void createProductos() {
        if (productoRepository.count() == 0) {
            Producto producto1 = new Producto();
            producto1.setNombre("Laptop");
            producto1.setDescripcion("Laptop de alta gama.");
            producto1.setPrecio(1500.00);
            producto1.setStock(50);

            Optional<Categoria> optionalCategoria = categoriaRepository.findByNombre("Electrónica");

            if (optionalCategoria.isPresent()) {
                Categoria categoria = optionalCategoria.get();
                producto1.setCategoria(categoria);
            }else{
                producto1.setCategoria(null);
            }
            productoRepository.save(producto1);

            Producto producto2 = new Producto();
            producto2.setNombre("Smartphone");
            producto2.setDescripcion("Smartphone con cámara de alta resolución.");
            producto2.setPrecio(800.00);
            producto2.setStock(100);

            optionalCategoria = categoriaRepository.findByNombre("Electrónica");
            if (optionalCategoria.isPresent()) {
                Categoria categoria = optionalCategoria.get();
                producto2.setCategoria(categoria);
            }else{
                producto2.setCategoria(null);
            }

            productoRepository.save(producto2);

            Producto producto3 = new Producto();
            producto3.setNombre("Whisky");
            producto3.setDescripcion("Whisky Johnnie Walker de El Eternauta 'Sigue caminando Juan'.");
            producto3.setPrecio(5000.00);
            producto3.setStock(200);
            optionalCategoria = categoriaRepository.findByNombre("Licores");
            if (optionalCategoria.isPresent()) {
                Categoria categoria = optionalCategoria.get();
                producto3.setCategoria(categoria);
            }else{
                producto3.setCategoria(null);
            }
            productoRepository.save(producto3);
        }
    }

}
