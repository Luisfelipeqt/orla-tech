package br.com.orla.tech.api.domain.mapper;

import br.com.orla.tech.api.domain.model.dto.request.funcionario.FuncionarioCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.response.funcionario.FuncionarioResponseDTO;
import br.com.orla.tech.api.domain.model.dto.response.projeto.ProjetoResponseDTO;
import br.com.orla.tech.api.domain.model.entity.FuncionarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class FuncionarioMapper {

    public Page<FuncionarioResponseDTO> convert(Page<FuncionarioEntity> entidades) {
        return entidades.map(this::convert);
    }

    public FuncionarioResponseDTO convert(FuncionarioEntity entidade) {
        var projetos = entidade.getProjetos().stream().map(p -> new ProjetoResponseDTO(
                p.getId(),
                p.getNome(),
                p.getDataCriacao(),
                null
        )).collect(Collectors.toSet());
        return new FuncionarioResponseDTO(
                entidade.getId(),
                entidade.getNome(),
                entidade.getCpf(),
                entidade.getEmail(),
                entidade.getSalario(),
                projetos
        );
    }

    public FuncionarioEntity convert(FuncionarioCreateRequestDTO dto) {
        return new FuncionarioEntity(
                dto.nome(),
                dto.cpf(),
                dto.email(),
                dto.salario(),
                new HashSet<>()
        );
    }
}
