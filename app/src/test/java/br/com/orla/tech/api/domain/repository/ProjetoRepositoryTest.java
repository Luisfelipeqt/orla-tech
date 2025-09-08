package br.com.orla.tech.api.domain.repository;

import br.com.orla.tech.api.domain.model.entity.ProjetoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjetoRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("orla-tech")
            .withUsername("orla-tech")
            .withPassword("orla-tech");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.default_schema", () -> "public");
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProjetoRepository repository;

    private List<ProjetoEntity> projetosSalvos;

    @BeforeEach
    void setUp() {
        entityManager.getEntityManager().createNativeQuery("CREATE SCHEMA IF NOT EXISTS orla_tech").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("SET search_path TO orla_tech, public").executeUpdate();

        entityManager.getEntityManager().createNativeQuery("TRUNCATE orla_tech.projetos RESTART IDENTITY CASCADE").executeUpdate();

        entityManager.flush();
        entityManager.clear();

        List<ProjetoEntity> projetos = List.of(
                ProjetoEntity.builder()
                        .nome("Sistema de Gestão Financeira")
                        .build(),
                ProjetoEntity.builder()
                        .nome("Portal do Cliente")
                        .build(),
                ProjetoEntity.builder()
                        .nome("API de Integração")
                        .build(),
                ProjetoEntity.builder()
                        .nome("Dashboard Analytics")
                        .build(),
                ProjetoEntity.builder()
                        .nome("App Mobile")
                        .build(),
                ProjetoEntity.builder()
                        .nome("Sistema de Vendas")
                        .build(),
                ProjetoEntity.builder()
                        .nome("Plataforma E-commerce")
                        .build(),
                ProjetoEntity.builder()
                        .nome("Sistema de RH")
                        .build(),
                ProjetoEntity.builder()
                        .nome("Portal Administrativo")
                        .build(),
                ProjetoEntity.builder()
                        .nome("Sistema de Relatórios")
                        .build()
        );

        projetosSalvos = repository.saveAll(projetos);
        entityManager.flush();
    }

    @DisplayName("Deve buscar projetos por IDs existentes")
    @Test
    void testBuscarProjetosPorIdsExistentes() {
        var idsParaBuscar = projetosSalvos.stream()
                .limit(3)
                .map(ProjetoEntity::getId)
                .toList();

        Set<ProjetoEntity> projetosEncontrados = repository.findAllByIdIn(idsParaBuscar);

        assertEquals(3, projetosEncontrados.size());
        assertThat(projetosEncontrados)
                .extracting(ProjetoEntity::getId)
                .containsExactlyInAnyOrderElementsOf(idsParaBuscar);
    }

    @DisplayName("Deve buscar projetos por IDs específicos do Banco")
    @Test
    void testBuscarProjetosPorIdsEspecificos() {
        var idsEspecificos = List.of(1, 3, 5);

        Set<ProjetoEntity> projetosEncontrados = repository.findAllByIdIn(idsEspecificos);

        assertThat(projetosEncontrados).hasSize(3);
        assertThat(projetosEncontrados)
                .extracting(ProjetoEntity::getId)
                .containsExactlyInAnyOrder(1, 3, 5);

        var projeto = projetosEncontrados.stream()
                .filter(p -> p.getId().equals(1))
                .findFirst()
                .orElse(null);

        assertNotNull(projeto);
        assertEquals("Sistema de Gestão Financeira", projeto.getNome());
    }

    @DisplayName("Deve retornar conjunto vazio quando buscar por IDs inexistentes")
    @Test
    void testRetornarVazioQuandoBuscarPorIdsInexistentes() {
        var idsInexistentes = List.of(999, 998, 997);

        Set<ProjetoEntity> projetosEncontrados = repository.findAllByIdIn(idsInexistentes);

        assertEquals(Collections.emptySet(), projetosEncontrados);
    }

    @DisplayName("Deve retornar projetos para IDs mistos (existentes e inexistentes)")
    @Test
    void testRetornarApenasExistentesParaIdsMistos() {
        var idExistente = projetosSalvos.getFirst().getId();
        var idsMistos = List.of(idExistente, 999, 998);

        Set<ProjetoEntity> projetosEncontrados = repository.findAllByIdIn(idsMistos);

        assertEquals(1, projetosEncontrados.size());
        assertEquals(idExistente, projetosEncontrados.iterator().next().getId());
    }

    @DisplayName("Deve contar corretamente o número de projetos")
    @Test
    void testContarCorretamenteNumeroProjetos() {
        long count = repository.count();

        assertEquals(10, count);
    }

    @DisplayName("Deve buscar todos os projetos salvos")
    @Test
    void testBuscarTodosOsProjetosSalvos() {
        var todosProjetos = repository.findAll();

        assertEquals(10, todosProjetos.size());
        assertThat(todosProjetos)
                .extracting(ProjetoEntity::getNome)
                .contains("Sistema de Gestão Financeira", "Portal do Cliente", "API de Integração");
    }

    @DisplayName("Deve deletar projeto por ID")
    @Test
    void testDeletarProjetoPorId() {
        var idParaDeletar = projetosSalvos.getFirst().getId();

        repository.deleteById(idParaDeletar);

        assertFalse(repository.existsById(idParaDeletar));
        assertEquals(9, repository.count());
    }

    @DisplayName("Deve buscar projetos usando Collection vazia")
    @Test
    void testTratarCollectionVazia() {
        Collection<Integer> idsVazios = List.of();

        Set<ProjetoEntity> resultado = repository.findAllByIdIn(idsVazios);

        assertEquals(emptySet(), resultado);
    }

    @DisplayName("Deve verificar integridade dos dados carregados do Banco")
    @Test
    void testVerificarIntegridadeDosDadosCarregados() {
        var projeto = repository.findById(1).orElse(null);

        assertNotNull(projeto);
        assertEquals("Sistema de Gestão Financeira", projeto.getNome());
        assertNotNull(projeto.getId());
    }

    @DisplayName("Deve salvar novo projeto")
    @Test
    void testSalvarNovoProjeto() {
        var novoProjeto = ProjetoEntity.builder()
                .nome("Projeto de Teste")
                .build();

        var projetoSalvo = repository.save(novoProjeto);

        assertNotNull(projetoSalvo.getId());
        assertEquals("Projeto de Teste", projetoSalvo.getNome());
        assertEquals(11, repository.count());
    }

    @DisplayName("Deve atualizar projeto existente")
    @Test
    void testAtualizarProjetoExistente() {
        var projeto = projetosSalvos.getFirst();
        var novoNome = "Sistema de Gestão Financeira - Atualizado";

        projeto.setNome(novoNome);
        var projetoAtualizado = repository.save(projeto);

        assertEquals(novoNome, projetoAtualizado.getNome());
        assertEquals(projeto.getId(), projetoAtualizado.getId());
    }

    @DisplayName("Deve verificar existência de projeto por ID")
    @Test
    void testVerificarExistenciaProjetoPorId() {
        var idExistente = projetosSalvos.getFirst().getId();
        var idInexistente = 999;

        assertThat(repository.existsById(idExistente)).isTrue();
        assertThat(repository.existsById(idInexistente)).isFalse();
    }
}