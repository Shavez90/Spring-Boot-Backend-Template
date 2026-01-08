package com.template.controller;

import com.template.dto.ApiResponse;
import com.template.dto.ProductDTO;
import com.template.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createProduct(
            @Valid @RequestBody ProductDTO productDTO) {
        log.info("Creating new product: {}", productDTO.getName());
        ProductDTO createdProduct = productService.createProduct(productDTO);
        ApiResponse<?> response = new ApiResponse<>(true, "Product created successfully", createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getProductById(
            @PathVariable Long id) {
        log.info("Fetching product with id: {}", id);
        ProductDTO product = productService.getProductById(id);
        ApiResponse<?> response = new ApiResponse<>(true, "Product retrieved successfully", product);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching all products - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> products = productService.getAllProducts(pageable);
        ApiResponse<?> response = new ApiResponse<>(true, "Products retrieved successfully", products);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchProducts(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Searching products by name: {}", name);
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> products = productService.searchProductsByName(name, pageable);
        ApiResponse<?> response = new ApiResponse<>(true, "Search completed successfully", products);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<?>> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching products by category: {}", category);
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> products = productService.getProductsByCategory(category, pageable);
        ApiResponse<?> response = new ApiResponse<>(true, "Products retrieved successfully", products);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<ApiResponse<?>> getInStockProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching in-stock products");
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> products = productService.getInStockProducts(pageable);
        ApiResponse<?> response = new ApiResponse<>(true, "In-stock products retrieved successfully", products);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        log.info("Updating product with id: {}", id);
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        ApiResponse<?> response = new ApiResponse<>(true, "Product updated successfully", updatedProduct);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteProduct(
            @PathVariable Long id) {
        log.info("Deleting product with id: {}", id);
        productService.deleteProduct(id);
        ApiResponse<?> response = new ApiResponse<>(true, "Product deleted successfully");
        return ResponseEntity.ok(response);
    }
}
