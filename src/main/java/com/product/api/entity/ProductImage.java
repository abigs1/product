package com.product.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_image")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    @JsonProperty("product_image_id")
    private Integer productImageId;

    @Column(name = "product_id", nullable = false)
    @JsonProperty("product_id")
    private Integer productId;

    @Column(name = "image", nullable = false, length = 255)
    @JsonProperty("image")
    private String image;

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private Integer status = 1;

    public ProductImage() {
    }

    public ProductImage(Integer productImageId, Integer productId,
            String image, Integer status) {
        this.productImageId = productImageId;
        this.productId = productId;
        this.image = image;
        this.status = status;
    }

    public Integer getProductImageId() {
        return productImageId;
    }

    public void setProductImageId(Integer productImageId) {
        this.productImageId = productImageId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
