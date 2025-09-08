package br.com.orla.tech.api.controller.rest;

import br.com.orla.tech.api.controller.api.IFuncionarioController;
import br.com.orla.tech.api.domain.model.dto.request.funcionario.FuncionarioCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.request.funcionario.FuncionarioFilterRequestDTO;
import br.com.orla.tech.api.domain.model.dto.response.funcionario.FuncionarioResponseDTO;
import br.com.orla.tech.api.domain.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class FuncionarioController implements IFuncionarioController {

    private final FuncionarioService service;

    @Override
    public ResponseEntity<Page<FuncionarioResponseDTO>> listar(String nome,
                                                               String cpf,
                                                               String email,
                                                               BigDecimal salarioInicial,
                                                               BigDecimal salarioFinal,
                                                               Pageable pageable) {
        var filter = new FuncionarioFilterRequestDTO(nome, cpf, email, salarioInicial, salarioFinal);
        return ResponseEntity.status(OK).body(service.listar(filter, pageable));
    }

    @Override
    public ResponseEntity<FuncionarioResponseDTO> cadastrar(FuncionarioCreateRequestDTO dto, UriComponentsBuilder uriBuilder) {
        var funcionarioCadastrado = service.cadastrar(dto);

        var location = uriBuilder.scheme("http").path("/v1/funcionarios/{id}")
                .buildAndExpand(funcionarioCadastrado.id()).toUri();

        return ResponseEntity.created(location).body(funcionarioCadastrado);
    }
}
