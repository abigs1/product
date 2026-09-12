package com.product;

// Clase Category (adaptada de la Práctica 1)
// Atributos:
//  - categoryId: identificador único, se genera automáticamente al crear la categoría.
//  - category: nombre de la categoría (debe ser único).
//  - tag: etiqueta corta de la categoría (debe ser única).
//  - parentCategoryId: id de la categoría "padre". Si no tiene padre, vale null.
//  - status: 1 = activa, 0 = eliminada (borrado lógico, no se borra físicamente).

public class Category {

    private Integer category_id;
    private String category;
    private String tag;
    private Integer parentCategoryId;
    private Integer status;

    // Constructor completo (categoría ya existente, con id conocido)
    public Category(Integer category_id, String category, String tag,
            Integer parentCategoryId, Integer status) {
        this.category_id = category_id;
        this.category = category;
        this.tag = tag;
        this.parentCategoryId = parentCategoryId;
        this.status = status;
    }

    // Constructor para generar una categoría nueva (el id lo asigna el service)
    public Category(String category, String tag, Integer parentCategoryId) {
        this.category = category;
        this.tag = tag;
        this.parentCategoryId = parentCategoryId;
        this.status = 1; // una categoría nueva nace activa
    }

    // Getters y Setters
    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategoryId(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" + category_id + ", \"" + category + "\", \"" + tag + "\", " + parentCategoryId + ", " + status + "}";
    }
}
