package br.com.orla.tech.api.domain.model.dto.request.projeto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Filtro para busca de projetos.")
public record ProjetoFilterRequestDTO(

        @Schema(description = "Nome do projeto")
        String nome,

        @Schema(description = "Data de início do projeto")
        LocalDate dataInicial,

        @Schema(description = "Data final do projeto")
        LocalDate dataFinal
) {
}