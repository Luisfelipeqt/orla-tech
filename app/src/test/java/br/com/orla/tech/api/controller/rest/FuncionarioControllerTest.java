package br.com.orla.tech.api.controller.rest;

import br.com.orla.tech.api.commons.generator.FuncionarioGenerator;
import br.com.orla.tech.api.domain.model.dto.request.funcionario.FuncionarioCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.request.funcionario.FuncionarioFilterRequestDTO;
import br.com.orla.tech.api.domain.model.dto.response.funcionario.FuncionarioResponseDTO;
import br.com.orla.tech.api.domain.service.FuncionarioService;
import br.com.orla.tech.api.support.exceptions.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class FuncionarioControllerTest {

    private final FuncionarioGenerator generator = new FuncionarioGenerator();

    @Mock
    private FuncionarioService service;

    @InjectMocks
    private FuncionarioController controller;

    private Page<FuncionarioResponseDTO> responsePageableDTO;

    private FuncionarioResponseDTO responseDTO;

    private FuncionarioCreateRequestDTO requestCreateDTO;

    @BeforeEach
    void setup() {
        responseDTO = generator.gerarResponseDTO();
        requestCreateDTO = generator.gerarCreateRequestDTOComProjetos();
        responsePageableDTO = generator.gerarPageableResponseDTO();
    }

    @DisplayName("Deve listar funcionários paginado com sucesso")
    @Test
    void testListarFuncionariosERetornarPaginadoComSucessoEHttpStatusCode200() {
        var pageable = PageRequest.of(0, 25);
        var filter = new FuncionarioFilterRequestDTO(null, null, null, null, null);

        when(service.listar(filter, pageable)).thenReturn(responsePageableDTO);

        var response = controller.listar(null, null, null, null, null, pageable);

        assertEquals(OK, response.getStatusCode());
        verify(service).listar(filter, pageable);
    }

    @DisplayName("Deve retornar NotFoundException ao listar funcionários quando não encontrar nenhum funcionário")
    @Test
    void testListarFuncionariosERetornarNotFoundExceptionQuandoNaoEncontrarNenhumFuncionario() {
        var pageable = PageRequest.of(0, 25);
        var filter = new FuncionarioFilterRequestDTO(null, null, null, null, null);

        when(service.listar(filter, pageable)).thenThrow(RecursoNaoEncontradoException.class);

        assertThrows(RecursoNaoEncontradoException.class, () -> controller.listar(null, null, null, null, null, pageable));

        verify(service).listar(filter, pageable);
    }

    @DisplayName("Deve registrar funcionário com sucesso")
    @Test
    void testCadastrarFuncionarioComSucessoEHttpStatusCode200() {
        String url = "http://localhost";
        var uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        when(service.cadastrar(requestCreateDTO)).thenReturn(responseDTO);

        var response = controller.cadastrar(requestCreateDTO, uriBuilder);

        var location = url + "/v1/funcionarios/" + responseDTO.id();

        assertEquals(responseDTO, response.getBody());
        assertEquals(CREATED, response.getStatusCode());
        assertEquals(11, responseDTO.projetos().size());
        assertThat(response.getHeaders().getLocation()).hasToString(location);

        verify(service).cadastrar(requestCreateDTO);
    }
}