package com.template.repository;

import com.template.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends BaseRepository<Product> {

    Optional<Product> findBySku(String sku);

    @Query("{ 'name': { $regex: ?0, $options: 'i' }, 'isActive': true }")
    Page<Product> searchByName(String name, Pageable pageable);

    @Query("{ 'category': ?0, 'isActive': true }")
    Page<Product> findByCategory(String category, Pageable pageable);

    @Query("{ 'quantity': { $gt: 0 }, 'isActive': true }")
    Page<Product> findInStock(Pageable pageable);
}
