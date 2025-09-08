package br.com.orla.tech.api.domain.repository;

import br.com.orla.tech.api.domain.model.entity.FuncionarioEntity;
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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FuncionarioRepositoryTest {

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
    private FuncionarioRepository repository;

    private List<FuncionarioEntity> funcionariosSalvos;

    @BeforeEach
    void setUp() {
        entityManager.getEntityManager().createNativeQuery("CREATE SCHEMA IF NOT EXISTS orla_tech").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("SET search_path TO orla_tech, public").executeUpdate();

        entityManager.getEntityManager().createNativeQuery("TRUNCATE orla_tech.funcionarios RESTART IDENTITY CASCADE").executeUpdate();

        entityManager.flush();
        entityManager.clear();

        List<FuncionarioEntity> funcionarios = List.of(
                FuncionarioEntity.builder()
                        .nome("Freida Witting")
                        .cpf("71636652883")
                        .email("hollis.zieme@hotmail.com")
                        .salario(new BigDecimal("5000.00"))
                        .build(),
                FuncionarioEntity.builder()
                        .nome("Noemy Pacocha")
                        .cpf("59086329101")
                        .email("noemy@test.com")
                        .salario(new BigDecimal("6000.00"))
                        .build(),
                FuncionarioEntity.builder()
                        .nome("Jaycee West")
                        .cpf("12345678901")
                        .email("jaycee@test.com")
                        .salario(new BigDecimal("7000.00"))
                        .build(),
                FuncionarioEntity.builder()
                        .nome("João Silva")
                        .cpf("11111111111")
                        .email("joao@test.com")
                        .salario(new BigDecimal("4000.00"))
                        .build(),
                FuncionarioEntity.builder()
                        .nome("Maria Santos")
                        .cpf("22222222222")
                        .email("maria@test.com")
                        .salario(new BigDecimal("5500.00"))
                        .build(),
                FuncionarioEntity.builder()
                        .nome("Pedro Costa")
                        .cpf("33333333333")
                        .email("pedro@test.com")
                        .salario(new BigDecimal("6500.00"))
                        .build(),
                FuncionarioEntity.builder()
                        .nome("Ana Lima")
                        .cpf("44444444444")
                        .email("ana@test.com")
                        .salario(new BigDecimal("7500.00"))
                        .build(),
                FuncionarioEntity.builder()
                        .nome("Carlos Souza")
                        .cpf("55555555555")
                        .email("carlos@test.com")
                        .salario(new BigDecimal("4500.00"))
                        .build(),
                FuncionarioEntity.builder()
                        .nome("Lucia Oliveira")
                        .cpf("66666666666")
                        .email("lucia@test.com")
                        .salario(new BigDecimal("5200.00"))
                        .build(),
                FuncionarioEntity.builder()
                        .nome("Roberto Alves")
                        .cpf("77777777777")
                        .email("roberto@test.com")
                        .salario(new BigDecimal("6800.00"))
                        .build()
        );

        funcionariosSalvos = repository.saveAll(funcionarios);
        entityManager.flush();
    }

    @DisplayName("Deve buscar funcionários por IDs existentes")
    @Test
    void testBuscarFuncionariosPorIdsExistentes() {
        var idsParaBuscar = funcionariosSalvos.stream()
                .limit(3)
                .map(FuncionarioEntity::getId)
                .toList();

        Set<FuncionarioEntity> funcionariosEncontrados = repository.findAllByIdIn(idsParaBuscar);

        assertEquals(3, funcionariosEncontrados.size());
        assertThat(funcionariosEncontrados)
                .extracting(FuncionarioEntity::getId)
                .containsExactlyInAnyOrderElementsOf(idsParaBuscar);
    }

    @DisplayName("Deve buscar funcionários por IDs específicos do Banco")
    @Test
    void testBuscarFuncionariosPorIdsEspecificos() {
        var idsEspecificos = List.of(1, 3, 5);

        Set<FuncionarioEntity> funcionariosEncontrados = repository.findAllByIdIn(idsEspecificos);

        assertThat(funcionariosEncontrados).hasSize(3);
        assertThat(funcionariosEncontrados)
                .extracting(FuncionarioEntity::getId)
                .containsExactlyInAnyOrder(1, 3, 5);

        var funcionario = funcionariosEncontrados.stream()
                .filter(f -> f.getId().equals(1))
                .findFirst()
                .orElse(null);

        assertNotNull(funcionario);
        assertEquals("Freida Witting", funcionario.getNome());
    }

    @DisplayName("Deve retornar conjunto vazio quando buscar por IDs inexistentes")
    @Test
    void testRetornarVazioQuandoBuscarPorIdsInexistentes() {
        var idsInexistentes = List.of(999, 998, 997);

        Set<FuncionarioEntity> funcionariosEncontrados = repository.findAllByIdIn(idsInexistentes);

        assertEquals(Collections.emptySet(), funcionariosEncontrados);
    }

    @DisplayName("Deve retornar funcionários para IDs mistos (existentes e inexistentes")
    @Test
    void testRetornarApenasExistentesParaIdsMistos() {
        var idExistente = funcionariosSalvos.getFirst().getId();
        var idsMistos = List.of(idExistente, 999, 998);

        Set<FuncionarioEntity> funcionariosEncontrados = repository.findAllByIdIn(idsMistos);

        assertEquals(1, funcionariosEncontrados.size());
        assertEquals(idExistente, funcionariosEncontrados.iterator().next().getId());
    }

    @DisplayName("Deve retornar true quando CPF existir")
    @Test
    void testRetornarTrueQuandoCpfExistir() {
        var cpfExistente = "71636652883";

        var existe = repository.existsByCpf(cpfExistente);

        assertTrue(existe);
    }

    @DisplayName("Deve retornar false quando CPF não existir")
    @Test
    void testRetornarFalseQuandoCpfNaoExistir() {
        var cpfInexistente = "12345678902";

        var existe = repository.existsByCpf(cpfInexistente);

        assertFalse(existe);
    }

    @DisplayName("Deve verificar existência de múltiplos CPFs")
    @Test
    void testVerificarExistenciaDeMultiplosCpfs() {
        var cpfsParaTestar = List.of(
                "71636652883",
                "59086329101"
        );

        assertTrue(repository.existsByCpf(cpfsParaTestar.get(0)));
        assertTrue(repository.existsByCpf(cpfsParaTestar.get(1)));
    }

    @DisplayName("Deve retornar true quando email existir")
    @Test
    void testRetornarTrueQuandoEmailExistir() {
        var emailExistente = "hollis.zieme@hotmail.com";

        var existe = repository.existsByEmail(emailExistente);

        assertTrue(existe);
    }

    @DisplayName("Deve retornar false quando email não existir")
    @Test
    void testRetornarFalseQuandoEmailNaoExistir() {
        var emailInexistente = "inexistente@teste.com";

        var existe = repository.existsByEmail(emailInexistente);

        assertFalse(existe);
    }

    @DisplayName("Deve ser case-sensitive para verificação de email")
    @Test
    void testSerCaseSensitiveParaEmail() {
        var emailOriginal = "hollis.zieme@hotmail.com";
        var emailMaiusculo = emailOriginal.toUpperCase();

        assertTrue(repository.existsByEmail(emailOriginal));
        assertFalse(repository.existsByEmail(emailMaiusculo));
    }

    @DisplayName("Deve contar corretamente o número de funcionários")
    @Test
    void testContarCorretamenteNumeroFuncionarios() {
        long count = repository.count();

        assertEquals(10, count);
    }

    @DisplayName("Deve buscar todos os funcionários salvos")
    @Test
    void testBuscarTodosOsFuncionariosSalvos() {
        var todosFuncionarios = repository.findAll();

        assertEquals(10, todosFuncionarios.size());
        assertThat(todosFuncionarios)
                .extracting(FuncionarioEntity::getNome)
                .contains("Freida Witting", "Noemy Pacocha", "Jaycee West");
    }

    @DisplayName("Deve deletar funcionário por ID")
    @Test
    void testDeletarFuncionarioPorId() {
        var idParaDeletar = funcionariosSalvos.getFirst().getId();

        repository.deleteById(idParaDeletar);

        assertFalse(repository.existsById(idParaDeletar));
        assertEquals(9, repository.count());
    }

    @DisplayName("Deve buscar funcionários usando Collection vazia")
    @Test
    void testTratarCollectionVazia() {
        Collection<Integer> idsVazios = List.of();

        Set<FuncionarioEntity> resultado = repository.findAllByIdIn(idsVazios);

        assertEquals(emptySet(), resultado);
    }

    @DisplayName("Deve verificar integridade dos dados carregados do Banco")
    @Test
    void testFindAllParaVerificarAIntegridadeDosDadosCarregados() {
        var funcionarios = repository.findAll();

        assertThat(funcionarios).allSatisfy(funcionario -> {
            assertThat(funcionario.getId()).isNotNull();
            assertThat(funcionario.getNome()).isNotBlank();
            assertThat(funcionario.getCpf()).isNotBlank().hasSize(11);
            assertThat(funcionario.getEmail()).isNotBlank().contains("@");
            assertThat(funcionario.getSalario()).isPositive();
        });
    }
}