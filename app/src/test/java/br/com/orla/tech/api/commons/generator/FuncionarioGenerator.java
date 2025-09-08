package br.com.orla.tech.api.commons.generator;

import br.com.orla.tech.api.commons.util.ObjectMapperFactory;
import br.com.orla.tech.api.commons.util.WrapperDTO;
import br.com.orla.tech.api.domain.model.dto.request.funcionario.FuncionarioCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.response.funcionario.FuncionarioResponseDTO;
import br.com.orla.tech.api.domain.model.entity.FuncionarioEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;
import java.util.List;

public class FuncionarioGenerator {

    private final ObjectMapper mapper;

    public FuncionarioGenerator() {
        this.mapper = ObjectMapperFactory.createObjectMapper();
    }

    public Page<FuncionarioResponseDTO> gerarPageableResponseDTO() {
        var resource = new ClassPathResource("json/dtos/funcionarios/funcionarios.json");
        try {
            var wrapper = mapper.readValue(resource.getFile(), new TypeReference<WrapperDTO<FuncionarioResponseDTO>>() {
            });
            return new PageImpl<>(wrapper.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FuncionarioEntity> gerarListaDeFuncionarios() {
        var resource = new ClassPathResource("json/entities/funcionarios/funcionario_lista.json");
        try {
            return mapper.readValue(
                    resource.getFile(),
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler JSON de funcionários", e);
        }
    }


    public FuncionarioResponseDTO gerarResponseDTO() {
        var resource = new ClassPathResource("json/dtos/funcionarios/funcionario_create_response_dto.json");
        try {
            return mapper.readValue(resource.getFile(), FuncionarioResponseDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FuncionarioCreateRequestDTO gerarCreateRequestDTOComProjetos() {
        var resource = new ClassPathResource("json/dtos/funcionarios/funcionario_create_request_dto_com_ids_projetos.json");
        try {
            return mapper.readValue(resource.getFile(), FuncionarioCreateRequestDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FuncionarioCreateRequestDTO gerarCreateRequestDTOSemProjetos() {
        var resource = new ClassPathResource("json/dtos/funcionarios/funcionario_create_request_dto_sem_ids_projetos.json");
        try {
            return mapper.readValue(resource.getFile(), FuncionarioCreateRequestDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FuncionarioEntity gerarEntidade() {
        var resource = new ClassPathResource("json/entities/funcionarios/funcionario.json");
        try {
            return mapper.readValue(resource.getFile(), FuncionarioEntity.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}