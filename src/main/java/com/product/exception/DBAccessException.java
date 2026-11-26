package com.product.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;

public class DBAccessException extends ApiException {

    private static final long serialVersionUID = 1L;

    public DBAccessException(DataAccessException e) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Error al acceder a la base de datos.");
        initCause(e);
    }
}
