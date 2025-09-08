package br.com.orla.tech.api.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Table(name = "funcionarios", schema = "orla_tech")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioEntity extends Auditor implements Serializable {

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "email", nullable = false, updatable = false, length = 254)
    private String email;

    @Column(name = "salario", nullable = false)
    private BigDecimal salario;

    @ManyToMany(mappedBy = "funcionarios")
    private Set<ProjetoEntity> projetos = new HashSet<>();
}