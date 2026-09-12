package com.product;

import java.util.ArrayList;
import java.util.List;

// Contiene la lista de categorías (en memoria, no persistente)
// y los métodos para operar sobre ella.
public class CategoryService {

    // Lista compartida en memoria
    private static final List<Category> categories = new ArrayList<>();

    // Contador para generar el categoryId
    private static int nextId = 1;

    // Bloque estático: se ejecuta una sola vez, al cargar la clase
    // (es decir, en tiempo de ejecución cuando arranca la aplicación).
    // Así garantizamos que la lista nunca esté vacía.
    static {
        createCategory(new Category("Ropa", "RP", null));
        createCategory(new Category("Calzado", "CLZD", 1));
        createCategory(new Category("Playeras", "PLY", 1));
        createCategory(new Category("Electrónica", "ELEC", null));
        createCategory(new Category("Celulares", "CEL", 4));
    }

    // Regresa solo las categorías activas (status = 1)
    public static List<Category> getCategories() {
        List<Category> activas = new ArrayList<>();
        for (Category c : categories) {
            if (c.getStatus() == 1) {
                activas.add(c);
            }
        }
        return activas;
    }

    // Da de alta una nueva categoría, asignándole un id autogenerado
    public static void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
    }
}
