package br.com.orla.tech.api.domain.service;

import br.com.orla.tech.api.commons.generator.FuncionarioGenerator;
import br.com.orla.tech.api.domain.mapper.FuncionarioMapper;
import br.com.orla.tech.api.domain.model.dto.request.funcionario.FuncionarioFilterRequestDTO;
import br.com.orla.tech.api.domain.model.entity.FuncionarioEntity;
import br.com.orla.tech.api.domain.repository.FuncionarioRepository;
import br.com.orla.tech.api.domain.repository.ProjetoRepository;
import br.com.orla.tech.api.support.exceptions.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

    private final FuncionarioGenerator generator = new FuncionarioGenerator();

    @Mock
    private FuncionarioMapper mapper;

    @Mock
    private FuncionarioRepository repository;

    @Mock
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private FuncionarioService service;

    private FuncionarioEntity funcionarioEntity;

    @BeforeEach
    void setup() {
        funcionarioEntity = generator.gerarEntidade();
    }

    @DisplayName("Deve listar todos os funcionários com sucesso")
    @Test
    void testListarTodosOsFuncionariosComSucesso() {
        var filter = new FuncionarioFilterRequestDTO(null, null, null, null, null);
        var pageable = PageRequest.of(0, 20);

        var funcionariosList = List.of(funcionarioEntity);
        var entitiesPage = new PageImpl<>(funcionariosList);
        var responseDTO = generator.gerarResponseDTO();
        var responsePage = new PageImpl<>(List.of(responseDTO));

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(entitiesPage);
        when(mapper.convert(entitiesPage)).thenReturn(responsePage);

        service.listar(filter, pageable);

        verify(repository).findAll(any(Specification.class), any(Pageable.class));
        verify(mapper).convert(entitiesPage);
    }

    @DisplayName("Deve lançar RecursoNaoEncontradoException quando não encontrar funcionários")
    @Test
    void testListarDeveGerarErroQuandoNaoEncontrarFuncionarios() {
        var filter = new FuncionarioFilterRequestDTO(null, null, null, null, null);
        Pageable pageable = PageRequest.of(0, 10);

        var entitiesPageVazia = new PageImpl<FuncionarioEntity>(List.of());

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(entitiesPageVazia);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.listar(filter, pageable));

        verify(repository).findAll(any(Specification.class), any(Pageable.class));
        verify(mapper, never()).convert(any(Page.class));
    }

    @DisplayName("Deve cadastrar um funcionário com sucesso")
    @Test
    void testCadastrarFuncionarioComSucesso() {
        var createDTO = generator.gerarCreateRequestDTOComProjetos();
        var responseDTO = generator.gerarResponseDTO();

        when(repository.existsByCpf(createDTO.cpf())).thenReturn(false);
        when(repository.existsByEmail(createDTO.email())).thenReturn(false);
        when(mapper.convert(createDTO)).thenReturn(funcionarioEntity);
        when(repository.save(funcionarioEntity)).thenReturn(funcionarioEntity);
        when(mapper.convert(funcionarioEntity)).thenReturn(responseDTO);

        service.cadastrar(createDTO);

        verify(repository).existsByCpf(createDTO.cpf());
        verify(repository).existsByEmail(createDTO.email());
        verify(mapper).convert(createDTO);
        verify(repository).save(funcionarioEntity);
        verify(mapper).convert(funcionarioEntity);
    }


    @DisplayName("Deve cadastrar um funcionário com sucesso com projetos")
    @Test
    void testCadastrarFuncionarioSemProjetos() {
        var createDTO = generator.gerarCreateRequestDTOSemProjetos();
        var responseDTO = generator.gerarResponseDTO();

        when(repository.existsByCpf(createDTO.cpf())).thenReturn(false);
        when(repository.existsByEmail(createDTO.email())).thenReturn(false);
        when(mapper.convert(createDTO)).thenReturn(funcionarioEntity);
        when(repository.save(funcionarioEntity)).thenReturn(funcionarioEntity);
        when(mapper.convert(funcionarioEntity)).thenReturn(responseDTO);

        service.cadastrar(createDTO);

        verify(repository).existsByCpf(createDTO.cpf());
        verify(repository).existsByEmail(createDTO.email());
        verify(mapper).convert(createDTO);
        verify(repository).save(funcionarioEntity);
        verify(mapper).convert(funcionarioEntity);
    }

    @DisplayName("Deve lançar NegocioException ao tentar cadastrar um funcionário com CPF já existente")
    @Test
    void testCadastrarDeveGerarErroQuandoCpfJaExistir() {
        var createDTO = generator.gerarCreateRequestDTOComProjetos();

        when(repository.existsByCpf(createDTO.cpf())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.cadastrar(createDTO));

        verify(repository).existsByCpf(createDTO.cpf());
        verify(repository, never()).existsByEmail(any());
        verify(mapper, never()).convert(any(FuncionarioEntity.class));
        verify(repository, never()).save(any(FuncionarioEntity.class));
        verify(mapper, never()).convert(any(FuncionarioEntity.class));
    }

    @DisplayName("Deve lançar NegocioException ao tentar cadastrar um funcionário com Email já existente")
    @Test
    void testCadastrarDeveGerarErroQuandoEmailJaExistir() {
        var createDTO = generator.gerarCreateRequestDTOComProjetos();

        when(repository.existsByCpf(createDTO.cpf())).thenReturn(false);
        when(repository.existsByEmail(createDTO.email())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.cadastrar(createDTO));

        verify(repository).existsByCpf(createDTO.cpf());
        verify(repository).existsByEmail(createDTO.email());
        verify(mapper, never()).convert(any(FuncionarioEntity.class));
        verify(repository, never()).save(any(FuncionarioEntity.class));
        verify(mapper, never()).convert(any(FuncionarioEntity.class));
    }
}