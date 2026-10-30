package com.product.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DtoCategoryIn {

    @JsonProperty("category")
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 50, message = "El nombre de la categoría no puede exceder 50 caracteres")
    private String category;

    // Opcional: null significa que la categoría no tiene padre
    @JsonProperty("parentCategoryId")
    private Integer parentCategoryId;

    @JsonProperty("tag")
    @NotBlank(message = "El tag de la categoría es obligatorio")
    @Size(max = 10, message = "El tag de la categoría no puede exceder 10 caracteres")
    private String tag;

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getParentCategoryId() { return parentCategoryId; }
    public void setParentCategoryId(Integer parentCategoryId) { this.parentCategoryId = parentCategoryId; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
}
