package br.com.orla.tech.api.domain.service;

import br.com.orla.tech.api.domain.mapper.ProjetoMapper;
import br.com.orla.tech.api.domain.model.dto.request.projeto.ProjetoCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.request.projeto.ProjetoFilterRequestDTO;
import br.com.orla.tech.api.domain.model.dto.response.projeto.ProjetoResponseDTO;
import br.com.orla.tech.api.domain.model.entity.ProjetoEntity;
import br.com.orla.tech.api.domain.repository.FuncionarioRepository;
import br.com.orla.tech.api.domain.repository.ProjetoRepository;
import br.com.orla.tech.api.domain.repository.spec.builders.ProjetoSpecBuilder;
import br.com.orla.tech.api.support.exceptions.NegocioException;
import br.com.orla.tech.api.support.exceptions.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjetoService {

    public static final String NENHUM_PROJETO_CADASTRADO = "Nenhum projeto cadastrado.";

    private final ProjetoMapper mapper;
    private final ProjetoRepository repository;
    private final FuncionarioRepository funcionarioRepository;

    public Page<ProjetoResponseDTO> listar(ProjetoFilterRequestDTO filter, Pageable pageable) {
        validar(filter);

        var spec = ProjetoSpecBuilder.build(filter);

        var buscarProjetos = repository.findAll(spec, pageable);

        if (buscarProjetos.getContent().isEmpty()) {
            throw new RecursoNaoEncontradoException(NENHUM_PROJETO_CADASTRADO);
        }

        return mapper.convert(buscarProjetos);
    }

    private void validar(ProjetoFilterRequestDTO filter) {
        if ((Objects.nonNull(filter.dataInicial())) ^ (Objects.nonNull(filter.dataFinal()))) {
            throw new NegocioException("Se uma data for preenchida, ambas as datas (inicial e final) são obrigatórias.");
        }

        if (Objects.nonNull(filter.dataInicial())) {
            if (filter.dataInicial().isAfter(filter.dataFinal())) {
                throw new NegocioException("A data inicial não pode ser posterior à data final.");
            }
        }
    }

    public ProjetoResponseDTO cadastrar(ProjetoCreateRequestDTO dto) {
        var converterParaEntidade = mapper.convert(dto);

        setFuncionarios(converterParaEntidade, dto);

        var cadastrarProjeto = repository.save(converterParaEntidade);

        return mapper.convert(cadastrarProjeto);
    }

    private void setFuncionarios(ProjetoEntity entity, ProjetoCreateRequestDTO dto) {
        if (isNotEmpty(dto.idFuncionarios())) {
            var funcionarios = funcionarioRepository.findAllByIdIn(dto.idFuncionarios());
            entity.setFuncionarios(funcionarios);
        }
    }
}