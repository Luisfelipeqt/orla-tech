package br.com.orla.tech.api.domain.repository;

import br.com.orla.tech.api.domain.model.entity.FuncionarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.Set;

public interface FuncionarioRepository extends JpaRepository<FuncionarioEntity, Integer>, JpaSpecificationExecutor<FuncionarioEntity> {
    Set<FuncionarioEntity> findAllByIdIn(Collection<Integer> ids);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
