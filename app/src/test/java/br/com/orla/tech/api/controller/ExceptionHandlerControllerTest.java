package br.com.orla.tech.api.controller;


import br.com.orla.tech.api.domain.model.dto.erro.ErrorDTO;
import br.com.orla.tech.api.support.exceptions.NegocioException;
import br.com.orla.tech.api.support.exceptions.RecursoExistenteException;
import br.com.orla.tech.api.support.exceptions.RecursoNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.UnexpectedTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerControllerTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ExceptionHandlerController controller;

    /* ############################################ 4XX CLIENT EXCEPTION ############################################ */

    @Test
    void testHandleConflictException() {
        var exception = new RecursoExistenteException("Erro de conflito");
        var expected = ResponseEntity.status(CONFLICT).body(ErrorDTO.builder().build());

        var result = controller.handleBadRequest(exception, request);
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }

    @Test
    void testHandleBadRequest() {
        var exception = new NegocioException("Erro de conflito");
        var expected = ResponseEntity.status(BAD_REQUEST).body(ErrorDTO.builder().build());

        var result = controller.handleBadRequest(exception, request);
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }

    @Test
    void testHandleRecursoNaoEncontradoPixException() {
        var exception = new RecursoNaoEncontradoException("Recurso não encontrado");
        var expected = ResponseEntity.status(NOT_FOUND).body(ErrorDTO.builder().build());

        var result = controller.handleRecursoNaoEncontradoPixException(exception, request);
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }

    @Test
    void testHandleHttpRequestMethodNotSupportedException() {
        var exception = new HttpRequestMethodNotSupportedException("Método não permitido");
        var expected = ResponseEntity.status(METHOD_NOT_ALLOWED).body(ErrorDTO.builder().build());

        var result = controller.handleHttpRequestMethodNotSupportedException(exception, request);
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }

    @Test
    void testHandleUnexpectedTypeException() {
        var exception = new UnexpectedTypeException("Tipo inesperado");
        var expected = ResponseEntity.status(UNPROCESSABLE_ENTITY).body(ErrorDTO.builder().build());

        var result = controller.handleUnexpectedTypeException(exception, request);
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }

    @Test
    void testTratarErroViolacaoConstraint() {
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        var violation = mock(ConstraintViolation.class);

        when(violation.getMessage()).thenReturn("Erro de violação de constraint");

        var propertyPath = mock(Path.class);
        when(violation.getPropertyPath()).thenReturn(propertyPath);
        when(propertyPath.toString()).thenReturn("campoExemplo");

        violations.add(violation);

        var exception = new ConstraintViolationException(violations);
        var expected = ResponseEntity.status(UNPROCESSABLE_ENTITY).body(ErrorDTO.builder().build());
        var result = controller.handleConstraintViolationException(exception, request);

        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }


    @Test
    void testHandleHttpClientErrorException() {
        var exception = new HttpClientErrorException(BAD_REQUEST);
        var expected = ResponseEntity.status(BAD_REQUEST).body(ErrorDTO.builder().build());

        var result = controller.handleHttpClientErrorException(exception, request);
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }

    /* ############################################ 5XX SERVER EXCEPTION ############################################ */

    @Test
    void testHandleException() {
        var exception = new Exception("Erro interno do servidor. Entre em contato com o suporte.");

        var expected = ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ErrorDTO.builder().message("Erro interno do servidor. Entre em contato com o suporte.").build()
        );

        var result = controller.handleException(exception, request);

        assertThat(expected.getBody()).isNotNull();
        assertThat(expected.getBody().getMessage()).isNotNull();

        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMessage()).isEqualTo(expected.getBody().getMessage());
    }
}