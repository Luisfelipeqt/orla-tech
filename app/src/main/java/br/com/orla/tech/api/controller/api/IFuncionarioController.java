package br.com.orla.tech.api.controller.api;

import br.com.orla.tech.api.domain.model.dto.request.funcionario.FuncionarioCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.response.funcionario.FuncionarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@Tag(name = "Funcionarios", description = "Endpoints para gerenciamento dos funcionarios")
@RequestMapping("/v1/funcionarios")
public interface IFuncionarioController {

    @Operation(summary = "Listar funcionarios.", description = "Endpoint para listar funcionarios.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Funcionários listados com sucesso.",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = FuncionarioResponseDTO.class))
                    )),
            @ApiResponse(responseCode = "400", description = "Requisição com formato inválido.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Funcionario não encontrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado no servidor.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço não está disponível no momento.", content = @Content)
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Page<FuncionarioResponseDTO>> listar(

            @Parameter(description = "Nome do funcionário.")
            @RequestParam(value = "nome", required = false)
            String nome,

            @Parameter(description = "Nome do funcionário.")
            @RequestParam(value = "cpf", required = false)
            String cpf,

            @Parameter(description = "Email do funcionario")
            @RequestParam(value = "email", required = false)
            String email,

            @Parameter(description = "Salario inicial do funcionario")
            @RequestParam(value = "salarioInicial", required = false)
            BigDecimal salarioInicial,

            @Parameter(description = "Salario final do funcionario")
            @RequestParam(value = "salarioFinal", required = false)
            BigDecimal salarioFinal,

            @PageableDefault(size = 100)
            Pageable pageable
    );

    @Operation(summary = "Criar funcionário.", description = "Endpoint para criar funcionário.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Funcionário criado com sucesso.",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = FuncionarioResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Requisição com formato inválido.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado no servidor.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço não está disponível no momento.", content = @Content)
    })
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<FuncionarioResponseDTO> cadastrar(
            @Valid
            @RequestBody
            FuncionarioCreateRequestDTO dto,
            UriComponentsBuilder uriBuilder
    );
}