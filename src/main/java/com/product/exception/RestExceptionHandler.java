package com.product.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // Errores de negocio lanzados desde el servicio
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ExceptionResponse> handleApiException(
            ApiException exception, WebRequest request) {

        ExceptionResponse response = build(exception.getStatus(), exception.getMessage(), request);
        return new ResponseEntity<>(response, response.getError());
    }

    // Dos peticiones simultáneas duplicaron nombre/tag y la BD lo rechazó (unique)
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ExceptionResponse> handleDataIntegrity(
            DataIntegrityViolationException exception, WebRequest request) {

        ExceptionResponse response = build(HttpStatus.CONFLICT,
                "El nombre o el tag de la categoría ya existe", request);
        return new ResponseEntity<>(response, response.getError());
    }

    // Cualquier otro fallo de BD: mensaje legible, sin exponer detalles de JDBC/SQL.
    // Se maneja aquí (y no con try/catch en el servicio) porque algunos errores
    // ocurren al hacer commit, fuera del método del servicio.
    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<ExceptionResponse> handleDataAccess(
            DataAccessException exception, WebRequest request) {

        ExceptionResponse response = build(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error al acceder a la base de datos.", request);
        return new ResponseEntity<>(response, response.getError());
    }

    // Falla @Valid en el body
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>(build(HttpStatus.BAD_REQUEST, message, request), HttpStatus.BAD_REQUEST);
    }

    // JSON mal formado o sin body
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        return new ResponseEntity<>(
                build(HttpStatus.BAD_REQUEST, "El cuerpo de la petición es inválido", request),
                HttpStatus.BAD_REQUEST);
    }

    // Por ejemplo /category/abc/childs
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        return new ResponseEntity<>(
                build(HttpStatus.BAD_REQUEST, "Uno de los parámetros de la petición es inválido", request),
                HttpStatus.BAD_REQUEST);
    }

    private ExceptionResponse build(HttpStatus status, String message, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse();

        response.setTimestamp(LocalDateTime.now());
        response.setStatus(status.value());
        response.setError(status);
        response.setMessage(message);
        response.setPath(
            ((ServletWebRequest) request)
                .getRequest()
                .getRequestURI()
        );
        return response;
    }
}
