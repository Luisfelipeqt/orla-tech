package br.com.orla.tech.api.controller;

import br.com.orla.tech.api.domain.model.dto.erro.ErrorDTO;
import br.com.orla.tech.api.support.exceptions.NegocioException;
import br.com.orla.tech.api.support.exceptions.RecursoExistenteException;
import br.com.orla.tech.api.support.exceptions.RecursoNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionHandlerController {

    /* ############################################ 4XX CLIENT EXCEPTION ############################################ */

    @ExceptionHandler(value = {RecursoExistenteException.class})
    public ResponseEntity<ErrorDTO> handleBadRequest(RecursoExistenteException e, HttpServletRequest request) {
        log.error("Erro ao tentar criar recurso: o recurso já existe.", e);
        var err = gerarError(request, CONFLICT, e.getMessage());
        return ResponseEntity.status(CONFLICT).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(value = {NegocioException.class, MissingRequestValueException.class})
    public ResponseEntity<ErrorDTO> handleBadRequest(Exception e, HttpServletRequest request) {
        log.error("Erro de validacao de regra de negocio.", e);
        var err = gerarError(request, BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler({RecursoNaoEncontradoException.class})
    public ResponseEntity<ErrorDTO> handleRecursoNaoEncontradoPixException(RecursoNaoEncontradoException e, HttpServletRequest request) {
        log.error("Recurso nao encontrado.", e);
        var err = gerarError(request, NOT_FOUND, e.getMessage());
        return ResponseEntity.status(NOT_FOUND).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("Metodo/verbo HTTP nao suportado.", e);
        var err = gerarError(request, METHOD_NOT_ALLOWED, e.getMessage());
        return ResponseEntity.status(METHOD_NOT_ALLOWED).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ErrorDTO> handleUnexpectedTypeException(UnexpectedTypeException e, HttpServletRequest request) {
        log.error("Tipo de dado inesperado.", e);
        var err = gerarError(request, UNPROCESSABLE_ENTITY, e.getMessage());
        return ResponseEntity.status(UNPROCESSABLE_ENTITY).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> tratarErroArgumentoInvalido(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("Dados invalidos.", e);
        var err = gerarError(request, BAD_REQUEST, "Dados invalidos.");

        List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
        fieldErrorList.forEach(f -> err.addViolation(f.getField(), f.getDefaultMessage()));

        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.error("Parametros invalidos.", e);
        var err = gerarError(request, UNPROCESSABLE_ENTITY, "Parametros invalidos.");

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        violations.forEach(f -> err.addViolation(f.getPropertyPath().toString(), f.getMessage()));

        return ResponseEntity.status(UNPROCESSABLE_ENTITY).contentType(APPLICATION_JSON).body(err);
    }

    @ExceptionHandler({HttpClientErrorException.class})
    public ResponseEntity<ErrorDTO> handleHttpClientErrorException(HttpClientErrorException e, HttpServletRequest request) {
        log.error("Requisicao invalida.", e);
        var status = HttpStatus.valueOf(e.getStatusCode().value());
        var err = gerarError(request, status, e.getMessage());
        return ResponseEntity.status(status).contentType(APPLICATION_JSON).body(err);
    }

    /* ############################################ 5XX SERVER EXCEPTION ############################################ */

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDTO> handleException(Exception e, HttpServletRequest request) {
        log.error("Erro de infraestrutura do servico.", e);
        var err = gerarError(request, INTERNAL_SERVER_ERROR, "Erro interno do servidor. Entre em contato com o suporte.");
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(APPLICATION_JSON).body(err);
    }

    private ErrorDTO gerarError(HttpServletRequest request, HttpStatus httpStatus, String message) {
        return ErrorDTO.builder()
                .timeStamp(LocalDateTime.now())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(message)
                .path(request.getServletPath())
                .violations(new ArrayList<>())
                .build();
    }
}