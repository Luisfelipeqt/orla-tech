package br.com.orla.tech.api.domain.repository.spec;

import br.com.orla.tech.api.domain.model.entity.FuncionarioEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public interface FuncionarioSpec {

    static Specification<FuncionarioEntity> nomeIgual(String nome) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    static Specification<FuncionarioEntity> cpfIgual(String cpf) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("cpf")), "%" + cpf.toLowerCase() + "%");
    }

    static Specification<FuncionarioEntity> emailIgual(String email) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }


    static Specification<FuncionarioEntity> entreSalarioInicialEFinal(BigDecimal salarioInicial, BigDecimal salarioFinal) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("salario"), salarioInicial, salarioFinal);
    }
}