package com.product.api.service;

import com.product.api.entity.Product;
import com.product.api.entity.ProductImage;
import com.product.api.repository.RepoProduct;
import com.product.api.repository.RepoProductImage;
import com.product.exception.ApiException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SvcProductImageImpl implements SvcProductImage {

    @Autowired
    private RepoProductImage repo;

    @Autowired
    private RepoProduct repoProduct;

    @Override
    public ResponseEntity<List<ProductImage>> getProductImages(Integer productId) {
        try {
            validateProductId(productId);

            List<ProductImage> images =
                    repo.findByProductIdAndStatus(productId, 1);

            return new ResponseEntity<>(images, HttpStatus.OK);

        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al acceder a la base de datos");
        }
    }

    @Override
    public ResponseEntity<String> createProductImage(
            Integer productId, ProductImage productImage) {
        try {
            validateProductId(productId);

            if (!productId.equals(productImage.getProductId())) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "El ID del producto no coincide con el de la URL");
            }

            productImage.setStatus(1);
            repo.save(productImage);

            return new ResponseEntity<>(
                    "La imagen ha sido registrada", HttpStatus.CREATED);

        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al acceder a la base de datos");
        }
    }

    @Override
    public ResponseEntity<String> deleteProductImage(
            Integer productId, Integer productImageId) {
        try {
            validateProductId(productId);

            ProductImage productImage = repo.findById(productImageId)
                    .orElseThrow(() -> new ApiException(
                            HttpStatus.NOT_FOUND,
                            "La imagen no existe"));

            if (!productImage.getProductId().equals(productId)) {
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "La imagen no pertenece a este producto");
            }

            if (productImage.getStatus() == 0) {
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "La imagen no existe");
            }

            productImage.setStatus(0);
            repo.save(productImage);

            return new ResponseEntity<>(
                    "La imagen ha sido eliminada", HttpStatus.OK);

        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al acceder a la base de datos");
        }
    }

    private void validateProductId(Integer productId) {
        try {
            Product product = repoProduct.findById(productId)
                    .orElseThrow(() -> new ApiException(
                            HttpStatus.NOT_FOUND,
                            "El producto no existe"));

        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al acceder a la base de datos");
        }
    }
}
