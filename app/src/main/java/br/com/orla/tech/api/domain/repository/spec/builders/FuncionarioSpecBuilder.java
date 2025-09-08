package br.com.orla.tech.api.domain.repository.spec.builders;

import br.com.orla.tech.api.domain.model.dto.request.funcionario.FuncionarioFilterRequestDTO;
import br.com.orla.tech.api.domain.model.entity.FuncionarioEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static br.com.orla.tech.api.domain.repository.spec.FuncionarioSpec.cpfIgual;
import static br.com.orla.tech.api.domain.repository.spec.FuncionarioSpec.emailIgual;
import static br.com.orla.tech.api.domain.repository.spec.FuncionarioSpec.entreSalarioInicialEFinal;
import static br.com.orla.tech.api.domain.repository.spec.FuncionarioSpec.nomeIgual;
import static java.util.Objects.nonNull;

@Component
public class FuncionarioSpecBuilder {

    private FuncionarioSpecBuilder() {
    }

    public static Specification<FuncionarioEntity> build(FuncionarioFilterRequestDTO dto) {
        Specification<FuncionarioEntity> spec = Specification.not(null);


        if (nonNull(dto.nome())) {
            spec = spec.and(nomeIgual(dto.nome()));
        }

        if (nonNull(dto.cpf())) {
            spec = spec.and(cpfIgual(dto.cpf()));
        }

        if (nonNull(dto.email())) {
            spec = spec.and(emailIgual(dto.email()));
        }

        if (nonNull(dto.salarioInicial()) && nonNull(dto.salarioFinal())) {
            spec = spec.and(entreSalarioInicialEFinal(dto.salarioInicial(), dto.salarioFinal()));
        }

        return spec;
    }
}
