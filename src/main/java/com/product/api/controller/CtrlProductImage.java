package com.product.api.controller;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.api.entity.ProductImage;
import com.product.api.service.SvcProductImage;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/{id}/image")
public class CtrlProductImage {

    @Autowired
    private SvcProductImage service;

    @GetMapping
    public ResponseEntity<List<ProductImage>> getProductImages(
            @PathVariable("id") Integer productId) {

        return service.getProductImages(productId);
    }

    @PostMapping
    public ResponseEntity<String> createProductImage(
            @PathVariable("id") Integer productId,
            @Valid @RequestBody DtoProductImageIn dto) {

        ProductImage productImage = new ProductImage();
        productImage.setProductId(dto.getProductId());
        productImage.setImage(dto.getImage());

        return service.createProductImage(productId, productImage);
    }

    @DeleteMapping("/{productImageId}")
    public ResponseEntity<String> deleteProductImage(
            @PathVariable("id") Integer productId,
            @PathVariable("productImageId") Integer productImageId) {

        return service.deleteProductImage(productId, productImageId);
    }
}
