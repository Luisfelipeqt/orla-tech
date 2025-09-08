package br.com.orla.tech.api.domain.repository;

import br.com.orla.tech.api.domain.model.entity.ProjetoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.Set;

public interface ProjetoRepository extends JpaRepository<ProjetoEntity, Integer>, JpaSpecificationExecutor<ProjetoEntity> {
    Set<ProjetoEntity> findAllByIdIn(Collection<Integer> ids);
}

