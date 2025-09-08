package br.com.orla.tech.api.domain.model.dto.erro;

import java.io.Serializable;

public record FieldErrorDTO(String fieldName, String reason) implements Serializable {
}