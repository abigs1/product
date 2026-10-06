package com.product.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.api.entity.Category;

public interface RepoCategory extends JpaRepository<Category, Integer> {

    List<Category> findByStatus(Integer status);

    List<Category> findByParentCategoryIdAndStatus(Integer parentCategoryId, Integer status);

    boolean existsByParentCategoryIdAndStatus(Integer parentCategoryId, Integer status);

    // Unicidad (sin distinguir mayúsculas) al crear
    boolean existsByCategoryIgnoreCase(String category);

    boolean existsByTagIgnoreCase(String tag);

    // Unicidad al actualizar: ignora la propia categoría
    boolean existsByCategoryIgnoreCaseAndCategoryIdNot(String category, Integer categoryId);

    boolean existsByTagIgnoreCaseAndCategoryIdNot(String tag, Integer categoryId);
}
