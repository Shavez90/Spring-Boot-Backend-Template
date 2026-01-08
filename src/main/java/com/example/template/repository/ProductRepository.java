package com.example.template.repository;

import com.example.template.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends BaseRepository<Product> {

    Optional<Product> findBySku(String sku);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> searchByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.category = :category")
    Page<Product> findByCategory(@Param("category") String category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.quantity > 0")
    Page<Product> findInStock(Pageable pageable);
}
