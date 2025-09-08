package br.com.orla.tech.api.domain.mapper;

import br.com.orla.tech.api.domain.model.dto.request.projeto.ProjetoCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.response.funcionario.FuncionarioResponseDTO;
import br.com.orla.tech.api.domain.model.dto.response.projeto.ProjetoResponseDTO;
import br.com.orla.tech.api.domain.model.entity.ProjetoEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class ProjetoMapper {
    public Page<ProjetoResponseDTO> convert(Page<ProjetoEntity> entidades) {
        return entidades.map(this::convert);
    }

    public ProjetoResponseDTO convert(ProjetoEntity entidade) {
        var funcionarios = entidade.getFuncionarios().stream().map(f -> new FuncionarioResponseDTO(
                f.getId(),
                f.getNome(),
                f.getCpf(),
                f.getEmail(),
                f.getSalario(),
                null
        )).collect(Collectors.toSet());
        return new ProjetoResponseDTO(
                entidade.getId(),
                entidade.getNome(),
                entidade.getDataCriacao(),
                funcionarios

        );
    }

    public ProjetoEntity convert(ProjetoCreateRequestDTO dto) {
        return new ProjetoEntity(dto.nome(), new HashSet<>());
    }
}