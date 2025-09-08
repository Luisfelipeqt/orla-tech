package br.com.orla.tech.api.domain.model.dto.request.funcionario;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Objeto com os filtros para consulta de funcionários.")
public record FuncionarioFilterRequestDTO(

        @Schema(description = "Nome do funcionário")
        String nome,

        @Schema(description = "CPF do funcionário")
        String cpf,

        @Schema(description = "Email do funcionário")
        String email,

        @Schema(description = "Salário inicial do funcionário")
        BigDecimal salarioInicial,

        @Schema(description = "Salário final do funcionário")
        BigDecimal salarioFinal
) {
}