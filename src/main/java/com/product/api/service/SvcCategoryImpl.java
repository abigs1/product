package com.product.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.api.dto.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.api.repository.RepoCategory;
import com.product.exception.ApiException;

@Service
@Transactional
public class SvcCategoryImpl implements SvcCategory {

    private static final int ACTIVE = 1;
    private static final int INACTIVE = 0;

    private final RepoCategory repoCategory;

    @Autowired
    public SvcCategoryImpl(RepoCategory repoCategory) {
        this.repoCategory = repoCategory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return repoCategory.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findActive() {
        return repoCategory.findByStatus(ACTIVE);
    }

    // Regresa las hijas activas de la categoría indicada
    @Override
    @Transactional(readOnly = true)
    public List<Category> findChilds(Integer id) {
        getOrThrow(id);
        return repoCategory.findByParentCategoryIdAndStatus(id, ACTIVE);
    }

    @Override
    public void create(DtoCategoryIn in) {
        String name = in.getCategory().trim();
        String tag = in.getTag().trim();

        if (repoCategory.existsByCategoryIgnoreCase(name)) {
            throw new ApiException(HttpStatus.CONFLICT, "El nombre de la categoría ya existe");
        }
        if (repoCategory.existsByTagIgnoreCase(tag)) {
            throw new ApiException(HttpStatus.CONFLICT, "El tag de la categoría ya existe");
        }
        validateParent(in.getParentCategoryId(), null);

        repoCategory.save(new Category(name, tag, in.getParentCategoryId()));
    }

    @Override
    public void update(DtoCategoryIn in, Integer id) {
        Category current = getOrThrow(id);
        String name = in.getCategory().trim();
        String tag = in.getTag().trim();

        if (repoCategory.existsByCategoryIgnoreCaseAndCategoryIdNot(name, id)) {
            throw new ApiException(HttpStatus.CONFLICT, "El nombre de la categoría ya existe");
        }
        if (repoCategory.existsByTagIgnoreCaseAndCategoryIdNot(tag, id)) {
            throw new ApiException(HttpStatus.CONFLICT, "El tag de la categoría ya existe");
        }
        validateParent(in.getParentCategoryId(), id);

        current.setCategory(name);
        current.setTag(tag);
        current.setParentCategoryId(in.getParentCategoryId());
        repoCategory.save(current);
    }

    @Override
    public void enable(Integer id) {
        Category c = getOrThrow(id);
        if (c.getStatus() == ACTIVE) {
            throw new ApiException(HttpStatus.CONFLICT, "La categoría ya está activa");
        }
        // No se puede reactivar una hija si su padre está inactivo
        if (c.getParentCategoryId() != null) {
            Category parent = repoCategory.findById(c.getParentCategoryId()).orElse(null);
            if (parent == null || parent.getStatus() != ACTIVE) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "No se puede activar la categoría porque su categoría padre está inactiva");
            }
        }
        c.setStatus(ACTIVE);
        repoCategory.save(c);
    }

    @Override
    public void disable(Integer id) {
        Category c = getOrThrow(id);
        if (c.getStatus() == INACTIVE) {
            throw new ApiException(HttpStatus.CONFLICT, "La categoría ya está inactiva");
        }
        if (repoCategory.existsByParentCategoryIdAndStatus(id, ACTIVE)) {
            throw new ApiException(HttpStatus.CONFLICT,
                    "No se puede desactivar la categoría porque tiene categorías hijas");
        }
        c.setStatus(INACTIVE);
        repoCategory.save(c);
    }

    // ---------- auxiliares ----------

    private Category getOrThrow(Integer id) {
        return repoCategory.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "La categoría no existe"));
    }

    /**
     * Si parentId no es null debe existir con status 1, no puede ser la propia
     * categoría ni una de sus descendientes (evita ciclos).
     * selfId es null cuando se está creando.
     */
    private void validateParent(Integer parentId, Integer selfId) {
        if (parentId == null) {
            return;
        }
        if (parentId.equals(selfId)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Una categoría no puede ser padre de sí misma");
        }
        Category parent = repoCategory.findById(parentId).orElse(null);
        if (parent == null || parent.getStatus() != ACTIVE) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La categoría padre no existe o no está activa");
        }
        if (selfId != null) {
            Integer ancestor = parent.getParentCategoryId();
            while (ancestor != null) {
                if (ancestor.equals(selfId)) {
                    throw new ApiException(HttpStatus.BAD_REQUEST,
                            "Una categoría no puede ser hija de una de sus descendientes");
                }
                ancestor = repoCategory.findById(ancestor).map(Category::getParentCategoryId).orElse(null);
            }
        }
    }
}
