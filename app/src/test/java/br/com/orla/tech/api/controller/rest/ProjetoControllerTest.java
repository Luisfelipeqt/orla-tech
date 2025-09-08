package br.com.orla.tech.api.controller.rest;

import br.com.orla.tech.api.commons.generator.ProjetoGenerator;
import br.com.orla.tech.api.domain.model.dto.request.projeto.ProjetoCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.request.projeto.ProjetoFilterRequestDTO;
import br.com.orla.tech.api.domain.model.dto.response.projeto.ProjetoResponseDTO;
import br.com.orla.tech.api.domain.service.ProjetoService;
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
class ProjetoControllerTest {

    private final ProjetoGenerator generator = new ProjetoGenerator();

    @Mock
    private ProjetoService service;

    @InjectMocks
    private ProjetoController controller;

    private Page<ProjetoResponseDTO> responsePageableDTO;

    private ProjetoCreateRequestDTO requestCreateDTO;

    private ProjetoResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        responseDTO = generator.gerarResponseDTO();
        responsePageableDTO = generator.gerarPageableResponseDTO();
        requestCreateDTO = generator.gerarCreateRequestDTOComFuncionarios();
    }


    @DisplayName("Deve listar projetos paginado com sucesso")
    @Test
    void testListarProjetosERetornarPaginadoComSucessoEHttpStatusCode200() {
        var pageable = PageRequest.of(0, 25);
        var filter = new ProjetoFilterRequestDTO(null, null, null);

        when(service.listar(filter, pageable)).thenReturn(responsePageableDTO);

        var response = controller.listar(null, null, null, pageable);

        assertEquals(OK, response.getStatusCode());
        verify(service).listar(filter, pageable);
    }

    @DisplayName("Deve retornar NotFoundException ao listar projetos quando não encontrar nenhum funcionário")
    @Test
    void testListarProjetoERetornarNotFoundExceptionQuandoNaoEncontrarNenhumFuncionario() {
        var pageable = PageRequest.of(0, 25);
        var filter = new ProjetoFilterRequestDTO(null, null, null);

        when(service.listar(filter, pageable)).thenThrow(RecursoNaoEncontradoException.class);

        assertThrows(RecursoNaoEncontradoException.class, () -> controller.listar(null, null, null, pageable));

        verify(service).listar(filter, pageable);
    }


    @DisplayName("Deve registrar projeto com sucesso")
    @Test
    void testCadastrarProjetoComSucessoEHttpStatusCode200() {
        String url = "http://localhost";
        var uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        when(service.cadastrar(requestCreateDTO)).thenReturn(responseDTO);

        var response = controller.cadastrar(requestCreateDTO, uriBuilder);

        var location = url + "/v1/projetos/" + responseDTO.id();

        assertEquals(responseDTO, response.getBody());
        assertEquals(CREATED, response.getStatusCode());
        assertEquals(12, responseDTO.funcionarios().size());
        assertThat(response.getHeaders().getLocation()).hasToString(location);

        verify(service).cadastrar(requestCreateDTO);
    }
}