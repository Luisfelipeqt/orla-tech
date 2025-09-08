package br.com.orla.tech.api;

import br.com.orla.tech.api.domain.model.entity.FuncionarioEntity;
import br.com.orla.tech.api.domain.model.entity.ProjetoEntity;
import br.com.orla.tech.api.domain.repository.FuncionarioRepository;
import br.com.orla.tech.api.domain.repository.ProjetoRepository;
import com.github.javafaker.Faker;
import io.github.felseje.cpf.CpfUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static java.util.TimeZone.getTimeZone;
import static java.util.TimeZone.setDefault;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@Slf4j
@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class ApiApplication {

    public static void main(String[] args) {
        setDefault(getTimeZone("America/Sao_Paulo"));
        SpringApplication.run(ApiApplication.class, args);
    }

    @Component
    @Profile("!test")
    @RequiredArgsConstructor
    static class DataInitializer implements CommandLineRunner {

        private final int MAXIMO_PROJETOS = 100;
        private final int MAXIMO_FUNCIONARIOS = 1000;

        private final Faker faker = new Faker();

        private final ProjetoRepository projetoRepository;
        private final FuncionarioRepository funcionarioRepository;

        @Override
        @Transactional
        public void run(String... args) {
            Set<ProjetoEntity> totalProjetos = new HashSet<>();
            Set<FuncionarioEntity> totalFuncionarios = new HashSet<>();

            for (int i = 0; i < MAXIMO_PROJETOS; i++) {
                var projeto = ProjetoEntity
                        .builder()
                        .nome(faker.pokemon().name() + " " + faker.company().name())
                        .build();
                totalProjetos.add(projeto);
            }

            for (int i = 0; i < MAXIMO_FUNCIONARIOS; i++) {
                var cpf = CpfUtils.generate();
                var funcionario = FuncionarioEntity
                        .builder()
                        .nome(faker.name().fullName())
                        .cpf(cpf)
                        .email(faker.internet().emailAddress())
                        .salario(BigDecimal.valueOf(faker.number().randomDouble(2, 1631, 100000)))
                        .build();
                totalFuncionarios.add(funcionario);
            }

            var projetosSalvos = projetoRepository.saveAll(totalProjetos);
            var funcionariosSalvos = funcionarioRepository.saveAll(totalFuncionarios);

            var funcionariosList = funcionariosSalvos.stream().toList();

            for (var projeto : projetosSalvos) {
                int qtdFuncionarios = faker.number().numberBetween(1, 6);
                for (int i = 0; i < qtdFuncionarios; i++) {
                    var funcionarioAleatorio = funcionariosList.get(faker.number().numberBetween(0, funcionariosList.size()));
                    projeto.getFuncionarios().add(funcionarioAleatorio);
                }
            }

            projetoRepository.saveAll(projetosSalvos);

            log.info("Dados iniciais criados com sucesso: {} projetos e {} funcionários",
                    projetosSalvos.size(), funcionariosSalvos.size());
        }
    }
}