package br.com.orla.tech.api.domain.model.dto.request.funcionario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "Objeto com as informações necessárias para criar um funcionário.")
public record FuncionarioCreateRequestDTO(

        @Schema(description = "Nome do funcionário", requiredMode = REQUIRED)
        @NotBlank
        String nome,

        @Schema(description = "CPF do funcionário", requiredMode = REQUIRED)
        @NotBlank
        @CPF
        String cpf,

        @Schema(description = "Email do funcionário", requiredMode = REQUIRED)
        @NotBlank
        @Email
        String email,

        @Schema(description = "Salário do funcionário", requiredMode = REQUIRED)
        @NotNull
        @Positive
        BigDecimal salario,

        @Schema(description = "Lista de IDs de projetos que podem ser associados. Somente serão adicionados projetos já existentes.")
        Set<Integer> idProjetos
) {
}