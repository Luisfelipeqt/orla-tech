package br.com.orla.tech.api.controller.rest;

import br.com.orla.tech.api.controller.api.IProjetoController;
import br.com.orla.tech.api.domain.model.dto.request.projeto.ProjetoCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.request.projeto.ProjetoFilterRequestDTO;
import br.com.orla.tech.api.domain.model.dto.response.projeto.ProjetoResponseDTO;
import br.com.orla.tech.api.domain.service.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class ProjetoController implements IProjetoController {

    private final ProjetoService service;

    @Override
    public ResponseEntity<Page<ProjetoResponseDTO>> listar(
            String nome,
            LocalDate dataInicial,
            LocalDate dataFinal,
            Pageable pageable
    ) {
        var filter = new ProjetoFilterRequestDTO(nome, dataInicial, dataFinal);
        return ResponseEntity.status(OK).body(service.listar(filter, pageable));
    }

    @Override
    public ResponseEntity<ProjetoResponseDTO> cadastrar(ProjetoCreateRequestDTO dto, UriComponentsBuilder uriBuilder) {
        var projetoCadastrado = service.cadastrar(dto);

        var location = uriBuilder.scheme("http").path("/v1/projetos/{id}")
                .buildAndExpand(projetoCadastrado.id()).toUri();

        return ResponseEntity.created(location).body(projetoCadastrado);
    }
}

