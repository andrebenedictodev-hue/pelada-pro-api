package com.assovio.peladapro.peladaproapi.common.error;

import com.assovio.peladapro.peladaproapi.domain.exception.ConflictOperationException;
import com.assovio.peladapro.peladaproapi.domain.exception.EntidadeNaoEncontradaException;
import com.assovio.peladapro.peladaproapi.domain.exception.NaoAutorizadoException;
import com.assovio.peladapro.peladaproapi.domain.exception.NegocioException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        List<ApiError.FieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ApiError.FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiError apiError = new ApiError();
        apiError.setStatus(status.value());
        apiError.setTimestamp(OffsetDateTime.now());
        apiError.setTitle("Validation failed");
        apiError.setFields(errors);

        logger.warn("Validation error: {}", ex.getMessage(), ex);

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            @NonNull HandlerMethodValidationException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        status = HttpStatus.BAD_REQUEST;

        List<ApiError.FieldError> errors = ex.getAllErrors()
                .stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> new ApiError.FieldError(((FieldError) error).getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiError apiError = new ApiError();
        apiError.setStatus(status.value());
        apiError.setTimestamp(OffsetDateTime.now());
        apiError.setTitle("Validation failed");
        apiError.setFields(errors);

        logger.warn("Handler method validation error: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<Object> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiError apiError = new ApiError();
        apiError.setStatus(status.value());
        apiError.setTimestamp(OffsetDateTime.now());
        apiError.setTitle("Resource not found");
        apiError.setMessage(ex.getMessage());

        logger.info("Not found: {}", ex.getMessage(), ex);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiError apiError = new ApiError();
        apiError.setStatus(status.value());
        apiError.setTimestamp(OffsetDateTime.now());
        apiError.setTitle("Business rule violation");
        apiError.setMessage(ex.getMessage());

        logger.warn("Business rule violation: {}", ex.getMessage(), ex);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NaoAutorizadoException.class)
    public ResponseEntity<Object> handleNaoAutorizado(NaoAutorizadoException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ApiError apiError = new ApiError();
        apiError.setStatus(status.value());
        apiError.setTimestamp(OffsetDateTime.now());
        apiError.setTitle("Unauthorized");
        apiError.setMessage(ex.getMessage());

        logger.warn("Unauthorized: {}", ex.getMessage(), ex);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ConflictOperationException.class)
    public ResponseEntity<Object> handleConflict(ConflictOperationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        ApiError apiError = new ApiError();
        apiError.setStatus(status.value());
        apiError.setTimestamp(OffsetDateTime.now());
        apiError.setTitle("Conflict");
        apiError.setMessage(ex.getMessage());

        logger.warn("Conflict: {}", ex.getMessage(), ex);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError apiError = new ApiError();
        apiError.setStatus(status.value());
        apiError.setTimestamp(OffsetDateTime.now());
        apiError.setTitle("Internal server error");
        apiError.setMessage("Unexpected error. Please try again.");

        logger.error("Unexpected error: {}", ex.getMessage(), ex);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }
}
