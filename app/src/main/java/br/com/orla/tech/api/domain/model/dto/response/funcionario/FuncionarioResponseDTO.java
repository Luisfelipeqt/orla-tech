package br.com.orla.tech.api.domain.model.dto.response.funcionario;

import br.com.orla.tech.api.domain.model.dto.response.projeto.ProjetoResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@JsonInclude(NON_EMPTY)
@Schema(description = "Objeto com as informações de resposta de um funcionário.")
public record FuncionarioResponseDTO(

        @Schema(description = "ID do funcionário", requiredMode = REQUIRED)
        Integer id,

        @Schema(description = "Nome do funcionário", requiredMode = REQUIRED)
        String nome,

        @Schema(description = "CPF do funcionário", requiredMode = REQUIRED)
        String cpf,

        @Schema(description = "Email do funcionário", requiredMode = REQUIRED)
        String email,

        @Schema(description = "Salário do funcionário", requiredMode = REQUIRED)
        BigDecimal salario,

        @Schema(description = "Projetos associados ao funcionário")
        Set<ProjetoResponseDTO> projetos
) {
}