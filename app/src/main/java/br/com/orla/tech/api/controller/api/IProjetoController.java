package br.com.orla.tech.api.controller.api;

import br.com.orla.tech.api.domain.model.dto.request.projeto.ProjetoCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.response.projeto.ProjetoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@Tag(name = "Projetos")
@RequestMapping("/v1/projetos")
public interface IProjetoController {

    @Operation(summary = "Listar projetos.", description = "Endpoint para listar projetos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso.",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProjetoResponseDTO.class))
                    )),
            @ApiResponse(responseCode = "400", description = "Requisição com formato inválido.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado no servidor.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço não está disponível no momento.", content = @Content)
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Page<ProjetoResponseDTO>> listar(

            @Parameter(description = "Nome do projeto", example = "Projeto X")
            @RequestParam(value = "nome", required = false)
            String nome,

            @Parameter(description = "Data de início do projeto")
            @RequestParam(value = "dataInicial", required = false)
            @DateTimeFormat(iso = DATE)
            LocalDate dataInicial,

            @Parameter(description = "Data final do projeto")
            @RequestParam(value = "dataFinal", required = false)
            @DateTimeFormat(iso = DATE)
            LocalDate dataFinal,

            Pageable pageable
    );

    @Operation(summary = "Criar um projeto.", description = "Endpoint para criar projeto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso.",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProjetoResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Requisição com formato inválido.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro inesperado no servidor.", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço não está disponível no momento.", content = @Content)
    })
    @PostMapping
    ResponseEntity<ProjetoResponseDTO> cadastrar(
            @RequestBody
            ProjetoCreateRequestDTO dto,
            UriComponentsBuilder uriBuilder);
}

