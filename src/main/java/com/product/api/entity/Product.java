
package com.product.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    @JsonProperty("product_id")
    private Integer productId;

    @Column(name = "gtin", nullable = false, unique = true, length = 13)
    @JsonProperty("gtin")
    private String gtin;

    @Column(name = "product", nullable = false, unique = true, length = 100)
    @JsonProperty("product")
    private String product;

    @Column(name = "description")
    @JsonProperty("description")
    private String description;

    @Column(name = "price", nullable = false)
    @JsonProperty("price")
    private Float price;

    @Column(name = "stock", nullable = false)
    @JsonProperty("stock")
    private Integer stock;

    @Column(name = "category_id", nullable = false)
    @JsonProperty("category_id")
    private Integer categoryId;

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private Integer status = 1;

    public Product() {
    }

    public Product(Integer productId, String gtin, String product,
            String description, Float price, Integer stock,
            Integer categoryId, Integer status) {
        this.productId = productId;
        this.gtin = gtin;
        this.product = product;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.status = status;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
