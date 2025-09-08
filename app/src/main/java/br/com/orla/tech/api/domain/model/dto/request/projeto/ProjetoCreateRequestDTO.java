package br.com.orla.tech.api.domain.model.dto.request.projeto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "Objeto com as informações necessárias para criar um projeto.")
public record ProjetoCreateRequestDTO(

        @Schema(description = "Nome do projeto", requiredMode = REQUIRED)
        @NotBlank
        @Size(max = 255)
        String nome,

        @Schema(description = "Lista de IDs dos funcionários a serem associados ao projeto")
        Set<Integer> idFuncionarios
) {
}