package com.template.service;

import com.template.dto.ProductDTO;
import com.template.entity.Product;
import com.template.exception.ResourceNotFoundException;
import com.template.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info("Creating product: {}", productDTO.getName());
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .quantity(productDTO.getQuantity())
                .category(productDTO.getCategory())
                .imageUrl(productDTO.getImageUrl())
                .sku(productDTO.getSku())
                .isActive(true)
                .build();
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        log.info("Updating product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setCategory(productDTO.getCategory());
        product.setImageUrl(productDTO.getImageUrl());
        
        Product updatedProduct = productRepository.save(product);
        return mapToDTO(updatedProduct);
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = productRepository.findByIdAndActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        log.info("Fetching all products");
        Page<Product> products = productRepository.findAllActive(pageable);
        return products.map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> searchProductsByName(String name, Pageable pageable) {
        log.info("Searching products by name: {}", name);
        Page<Product> products = productRepository.searchByName(name, pageable);
        return products.map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsByCategory(String category, Pageable pageable) {
        log.info("Fetching products by category: {}", category);
        Page<Product> products = productRepository.findByCategory(category, pageable);
        return products.map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getInStockProducts(Pageable pageable) {
        log.info("Fetching in-stock products");
        Page<Product> products = productRepository.findInStock(pageable);
        return products.map(this::mapToDTO);
    }

    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setIsActive(false);
        productRepository.save(product);
    }

    private ProductDTO mapToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .sku(product.getSku())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .createdBy(product.getCreatedBy())
                .updatedBy(product.getUpdatedBy())
                .isActive(product.getIsActive())
                .build();
    }
}
