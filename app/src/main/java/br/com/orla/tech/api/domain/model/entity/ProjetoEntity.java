package br.com.orla.tech.api.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Table(name = "projetos", schema = "orla_tech")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
public class ProjetoEntity extends Auditor implements Serializable {


    @Column(name = "nome", nullable = false)
    private String nome;

    @ManyToMany()
    @JoinTable(
            name = "projetos_funcionarios",
            schema = "orla_tech",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "funcionario_id")
    )
    private Set<FuncionarioEntity> funcionarios = new HashSet<>();

    @Builder
    public ProjetoEntity(String nome, Set<FuncionarioEntity> funcionarios) {
        this.nome = nome;
        this.funcionarios = funcionarios != null ? funcionarios : new HashSet<>();
    }
}
