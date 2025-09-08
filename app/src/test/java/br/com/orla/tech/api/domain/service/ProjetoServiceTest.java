package br.com.orla.tech.api.domain.service;

import br.com.orla.tech.api.commons.generator.FuncionarioGenerator;
import br.com.orla.tech.api.commons.generator.ProjetoGenerator;
import br.com.orla.tech.api.domain.mapper.ProjetoMapper;
import br.com.orla.tech.api.domain.model.dto.request.projeto.ProjetoCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.request.projeto.ProjetoFilterRequestDTO;
import br.com.orla.tech.api.domain.model.entity.FuncionarioEntity;
import br.com.orla.tech.api.domain.model.entity.ProjetoEntity;
import br.com.orla.tech.api.domain.repository.FuncionarioRepository;
import br.com.orla.tech.api.domain.repository.ProjetoRepository;
import br.com.orla.tech.api.support.exceptions.NegocioException;
import br.com.orla.tech.api.support.exceptions.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjetoServiceTest {

    private ProjetoGenerator generator = new ProjetoGenerator();

    private FuncionarioGenerator funcionarioGenerator = new FuncionarioGenerator();

    @Mock
    private ProjetoMapper mapper;

    @Mock
    private ProjetoRepository repository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private ProjetoService service;

    private ProjetoEntity projetoEntity;

    private FuncionarioEntity funcionarioEntity;

    @BeforeEach
    void setup() {
        projetoEntity = generator.gerarEntidade();
        funcionarioEntity = funcionarioGenerator.gerarEntidade();
    }

    @DisplayName("Deve listar projetos com sucesso")
    @Test
    void testListarProjetosComSucesso() {
        var filter = new ProjetoFilterRequestDTO(null, null, null);
        Pageable pageable = PageRequest.of(0, 10);

        var projetosList = List.of(projetoEntity);
        var pageFromRepository = new PageImpl<>(projetosList);
        var responsePage = generator.gerarPageableResponseDTO();

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageFromRepository);
        when(mapper.convert(pageFromRepository)).thenReturn(responsePage);

        var result = service.listar(filter, pageable);

        assertNotNull(result);
        assertEquals(responsePage, result);
        verify(repository).findAll(any(Specification.class), any(Pageable.class));
        verify(mapper).convert(pageFromRepository);
    }

    @DisplayName("Deve lançar exceção ao listar projetos vazios")
    @Test
    void testListarProjetosVazio() {
        var filter = new ProjetoFilterRequestDTO(null, null, null);
        var pageable = PageRequest.of(0, 10);

        var emptyPage = new PageImpl<>(List.of());
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.listar(filter, pageable));
    }

    @DisplayName("Deve cadastrar projeto com funcionários com sucesso")
    @Test
    void testCadastrarProjetoComFuncionariosComSucesso() {
        var request = generator.gerarCreateRequestDTOComFuncionarios();
        var funcionarios = Set.of(funcionarioEntity);
        var entityConvertida = ProjetoEntity.builder().nome(request.nome()).funcionarios(Set.of()).build();
        var entityComFuncionarios = ProjetoEntity.builder().nome(request.nome()).funcionarios(funcionarios).build();
        var response = generator.gerarResponseDTO();

        when(mapper.convert(request)).thenReturn(entityConvertida);
        when(funcionarioRepository.findAllByIdIn(request.idFuncionarios())).thenReturn(funcionarios);
        when(repository.save(any(ProjetoEntity.class))).thenReturn(entityComFuncionarios);
        when(mapper.convert(entityComFuncionarios)).thenReturn(response);

        service.cadastrar(request);

        verify(funcionarioRepository).findAllByIdIn(request.idFuncionarios());
        verify(repository).save(any(ProjetoEntity.class));
        verify(mapper).convert(request);
        verify(mapper).convert(entityComFuncionarios);
    }

    @DisplayName("Deve cadastrar projeto sem funcionários com sucesso")
    @Test
    void testCadastrarProjetoSemFuncionariosComSucesso() {
        var request = new ProjetoCreateRequestDTO("Projeto Sem Funcionarios", null);
        var entitySemFuncionarios = ProjetoEntity.builder().nome(request.nome()).funcionarios(Set.of()).build();
        var response = generator.gerarResponseDTO();

        when(mapper.convert(request)).thenReturn(entitySemFuncionarios);
        when(repository.save(any(ProjetoEntity.class))).thenReturn(entitySemFuncionarios);
        when(mapper.convert(entitySemFuncionarios)).thenReturn(response);

        service.cadastrar(request);

        verify(funcionarioRepository, never()).findAllByIdIn(any());
        verify(repository).save(any(ProjetoEntity.class));
        verify(mapper).convert(request);
        verify(mapper).convert(entitySemFuncionarios);
    }

    @DisplayName("Deve cadastrar projeto mesmo quando funcionários não são encontrados")
    @Test
    void testCadastrarProjetoComFuncionarioInexistente() {
        var request = generator.gerarCreateRequestDTOComFuncionarios();
        var entityConvertida = ProjetoEntity.builder().nome(request.nome()).funcionarios(Set.of()).build();
        var entitySalva = ProjetoEntity.builder().nome(request.nome()).funcionarios(Set.of()).build();
        var response = generator.gerarResponseDTO();

        when(mapper.convert(request)).thenReturn(entityConvertida);
        when(funcionarioRepository.findAllByIdIn(request.idFuncionarios())).thenReturn(Set.of());
        when(repository.save(any(ProjetoEntity.class))).thenReturn(entitySalva);
        when(mapper.convert(entitySalva)).thenReturn(response);

        service.cadastrar(request);

        verify(funcionarioRepository).findAllByIdIn(request.idFuncionarios());
        verify(repository).save(any(ProjetoEntity.class));
        verify(mapper).convert(request);
        verify(mapper).convert(entitySalva);
    }

    @DisplayName("Deve retornar erro ao listar projetos com data inicial maior que a final")
    @Test
    void testListarMasRetornaNegocioExceptionQuandoADataInicialEPosteriorAFinal() {
        var dataInicial = LocalDate.now().plusDays(5);
        var dataFinal = LocalDate.now();
        var filter = new ProjetoFilterRequestDTO(null, dataInicial, dataFinal);
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(NegocioException.class, () -> service.listar(filter, pageable));

        verify(repository, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    @DisplayName("Deve retornar erro ao listar projetos com somente data final preenchida")
    @Test
    void testListarMasRetornaNegocioExceptionPorApenasUmaDataPreenchida() {
        var filter = new ProjetoFilterRequestDTO(null, null, LocalDate.now());
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(NegocioException.class, () -> service.listar(filter, pageable));
    }
}