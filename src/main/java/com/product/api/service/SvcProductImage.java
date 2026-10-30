package com.product.api.service;

import com.product.api.entity.ProductImage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SvcProductImage {

    ResponseEntity<List<ProductImage>> getProductImages(Integer productId);

    ResponseEntity<String> createProductImage(Integer productId, ProductImage productImage);

    ResponseEntity<String> deleteProductImage(Integer productId, Integer productImageId);

}
