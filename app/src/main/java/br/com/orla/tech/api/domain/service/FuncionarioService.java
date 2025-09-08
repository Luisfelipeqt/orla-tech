package br.com.orla.tech.api.domain.service;

import br.com.orla.tech.api.domain.mapper.FuncionarioMapper;
import br.com.orla.tech.api.domain.model.dto.request.funcionario.FuncionarioCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.request.funcionario.FuncionarioFilterRequestDTO;
import br.com.orla.tech.api.domain.model.dto.response.funcionario.FuncionarioResponseDTO;
import br.com.orla.tech.api.domain.model.entity.FuncionarioEntity;
import br.com.orla.tech.api.domain.repository.FuncionarioRepository;
import br.com.orla.tech.api.domain.repository.ProjetoRepository;
import br.com.orla.tech.api.domain.repository.spec.builders.FuncionarioSpecBuilder;
import br.com.orla.tech.api.support.exceptions.NegocioException;
import br.com.orla.tech.api.support.exceptions.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuncionarioService {

    public static final String NENHUM_FUNCIONARIO_CADASTRADO = "Nenhum funcionário cadastrado.";
    public static final String FUNCIONARIO_JA_EXISTENTE = "Já existe um funcionário cadastrado com esse %s.";

    private final FuncionarioMapper mapper;
    private final FuncionarioRepository repository;
    private final ProjetoRepository projetoRepository;

    public Page<FuncionarioResponseDTO> listar(FuncionarioFilterRequestDTO filter, Pageable pageable) {
        var spec = FuncionarioSpecBuilder.build(filter);

        var buscarFuncionarios = repository.findAll(spec, pageable);

        if (buscarFuncionarios.getContent().isEmpty()) {
            throw new RecursoNaoEncontradoException(NENHUM_FUNCIONARIO_CADASTRADO);
        }

        return mapper.convert(buscarFuncionarios);
    }

    public FuncionarioResponseDTO cadastrar(FuncionarioCreateRequestDTO dto) {
        verificarSeFuncionarioJaExiste(dto);

        var converterParaEntidade = mapper.convert(dto);

        setProjetos(converterParaEntidade, dto);

        var cadastrarFuncionario = repository.save(converterParaEntidade);

        return mapper.convert(cadastrarFuncionario);
    }

    private void verificarSeFuncionarioJaExiste(FuncionarioCreateRequestDTO dto) {
        var verificarSeCpfJaExiste = repository.existsByCpf(dto.cpf());

        if (verificarSeCpfJaExiste) {
            throw new NegocioException(format(FUNCIONARIO_JA_EXISTENTE, "CPF"));
        }

        var verificarSeEmailJaExiste = repository.existsByEmail(dto.email());

        if (verificarSeEmailJaExiste) {
            throw new NegocioException(format(FUNCIONARIO_JA_EXISTENTE, "Email"));
        }
    }

    private void setProjetos(FuncionarioEntity entity, FuncionarioCreateRequestDTO dto) {
        if (isNotEmpty(dto.idProjetos())) {
            var projetos = projetoRepository.findAllByIdIn(dto.idProjetos());
            entity.setProjetos(projetos);
        }
    }
}