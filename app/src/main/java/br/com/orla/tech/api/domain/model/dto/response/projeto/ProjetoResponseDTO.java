package br.com.orla.tech.api.domain.model.dto.response.projeto;

import br.com.orla.tech.api.domain.model.dto.response.funcionario.FuncionarioResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@JsonInclude(NON_EMPTY)
@Schema(description = "Objeto com as informações de resposta de um projeto.")
public record ProjetoResponseDTO(

        @Schema(description = "ID do projeto", requiredMode = REQUIRED)
        Integer id,

        @Schema(description = "Nome do projeto", requiredMode = REQUIRED)
        String nome,

        @Schema(description = "Data de início do projeto", requiredMode = REQUIRED)
        LocalDateTime dataInicio,

        @Schema(description = "Funcionários associados ao projeto")
        Set<FuncionarioResponseDTO> funcionarios
) {
}
