package com.product.api.repository;

import com.product.api.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoProductImage extends JpaRepository<ProductImage, Integer> {

    List<ProductImage> findByProductIdAndStatus(Integer productId, Integer status);

}
