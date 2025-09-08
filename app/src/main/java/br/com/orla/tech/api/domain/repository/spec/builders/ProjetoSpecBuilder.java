package br.com.orla.tech.api.domain.repository.spec.builders;

import br.com.orla.tech.api.domain.model.dto.request.projeto.ProjetoFilterRequestDTO;
import br.com.orla.tech.api.domain.model.entity.ProjetoEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static br.com.orla.tech.api.domain.repository.spec.ProjetoSpec.entreDataInicialEFinal;
import static br.com.orla.tech.api.domain.repository.spec.ProjetoSpec.nomeIgual;
import static java.util.Objects.nonNull;

@Component
public class ProjetoSpecBuilder {
    private ProjetoSpecBuilder() {
    }

    public static Specification<ProjetoEntity> build(ProjetoFilterRequestDTO dto) {
        Specification<ProjetoEntity> spec = Specification.not(null);

        if (nonNull(dto.nome())) {
            spec = spec.and(nomeIgual(dto.nome()));
        }

        if (nonNull(dto.dataInicial()) && nonNull(dto.dataFinal())) {
            spec = spec.and(entreDataInicialEFinal(dto.dataInicial(), dto.dataFinal()));
        }
        return spec;
    }
}

