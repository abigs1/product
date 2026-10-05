package com.product.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    @JsonProperty("category_id")
    private Integer categoryId;

    @Column(name = "category", nullable = false, unique = true, length = 50)
    @JsonProperty("category")
    private String category;

    @Column(name = "tag", nullable = false, unique = true, length = 10)
    @JsonProperty("tag")
    private String tag;

    @Column(name = "parent_category_id")
    @JsonProperty("parentCategoryId")
    private Integer parentCategoryId;

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private Integer status;

    public Category() {
        // JPA necesita un constructor vacío
    }

    public Category(Integer categoryId, String category, String tag,
            Integer parentCategoryId, Integer status) {
        this.categoryId = categoryId;
        this.category = category;
        this.tag = tag;
        this.parentCategoryId = parentCategoryId;
        this.status = status;
    }

    public Category(String category, String tag, Integer parentCategoryId) {
        this.category = category;
        this.tag = tag;
        this.parentCategoryId = parentCategoryId;
        this.status = 1;
    }

    // Una categoría nueva siempre nace activa
    @PrePersist
    void prePersist() {
        if (status == null) {
            status = 1;
        }
    }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public Integer getParentCategoryId() { return parentCategoryId; }
    public void setParentCategoryId(Integer parentCategoryId) { this.parentCategoryId = parentCategoryId; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    @Override
    public String toString() {
        return "{" + categoryId + ", \"" + category + "\", \"" + tag + "\", " + parentCategoryId + ", " + status + "}";
    }
}
