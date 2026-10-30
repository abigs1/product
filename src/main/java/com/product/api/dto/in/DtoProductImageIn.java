package com.product.api.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class DtoProductImageIn {

    @NotNull
    @Positive
    @JsonProperty("product_id")
    private Integer productId;

    @NotBlank
    @Size(max = 255)
    @JsonProperty("image")
    private String image;

    public DtoProductImageIn() {
    }

    public DtoProductImageIn(Integer productId, String image) {
        this.productId = productId;
        this.image = image;
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
}
