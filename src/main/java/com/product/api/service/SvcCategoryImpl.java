package com.product.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.product.api.entity.Category;
import com.product.api.repository.RepoCategory;
import com.product.exception.ApiException;

@Service
public class SvcCategoryImpl implements SvcCategory {

    private final RepoCategory repoCategory;

    @Autowired
    public SvcCategoryImpl(RepoCategory repoCategory) {
        this.repoCategory = repoCategory;
    }

    @Override
    public ResponseEntity<List<Category>> getCategories() {

        try {
            return ResponseEntity.ok(repoCategory.getCategories());

        } catch (DataAccessException e) {
            throw new ApiException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
            );
        }
    }
}
