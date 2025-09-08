package br.com.orla.tech.api.domain.repository.spec;

import br.com.orla.tech.api.domain.model.entity.ProjetoEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public interface ProjetoSpec {
    static Specification<ProjetoEntity> nomeIgual(String nome) {
        return (root, query, cb)
                -> cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    static Specification<ProjetoEntity> entreDataInicialEFinal(LocalDate dataInicial, LocalDate dataFinal) {
        return (root, query, cb) -> {
            var inicioDoDia = dataInicial.atStartOfDay();
            var fimDoDia = dataFinal.atTime(23, 59, 59, 999999999);

            return cb.between(root.get("dataCriacao"), inicioDoDia, fimDoDia);
        };
    }
}

