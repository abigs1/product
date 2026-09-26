package com.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CtrlProduct {

    // GET /category
    // Regresa el arreglo de categorías activas, en memoria (no persistente).
    @GetMapping
    public Category[] getCategories() {
        List<Category> categorias = CategoryService.getCategories();
        return categorias.toArray(new Category[0]);
    }
}
