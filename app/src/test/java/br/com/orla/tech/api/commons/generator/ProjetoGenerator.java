package br.com.orla.tech.api.commons.generator;

import br.com.orla.tech.api.commons.util.ObjectMapperFactory;
import br.com.orla.tech.api.commons.util.WrapperDTO;
import br.com.orla.tech.api.domain.model.dto.request.projeto.ProjetoCreateRequestDTO;
import br.com.orla.tech.api.domain.model.dto.response.projeto.ProjetoResponseDTO;
import br.com.orla.tech.api.domain.model.entity.ProjetoEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;

public class ProjetoGenerator {

    private final ObjectMapper mapper;

    public ProjetoGenerator() {
        this.mapper = ObjectMapperFactory.createObjectMapper();
    }

    public Page<ProjetoResponseDTO> gerarPageableResponseDTO() {
        var resource = new ClassPathResource("json/dtos/projetos/projetos.json");
        try {
            var wrapper = mapper.readValue(resource.getFile(), new TypeReference<WrapperDTO<ProjetoResponseDTO>>() {
            });
            return new PageImpl<>(wrapper.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ProjetoResponseDTO gerarResponseDTO() {
        var resource = new ClassPathResource("json/dtos/projetos/projeto_create_response_dto.json");
        try {
            return mapper.readValue(resource.getFile(), ProjetoResponseDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ProjetoCreateRequestDTO gerarCreateRequestDTOComFuncionarios() {
        var resource = new ClassPathResource("json/dtos/projetos/projeto_create_request_dto_com_ids_funcionarios.json");
        try {
            return mapper.readValue(resource.getFile(), ProjetoCreateRequestDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ProjetoEntity gerarEntidade() {
        var resource = new ClassPathResource("json/entities/projetos/projeto.json");
        try {
            return mapper.readValue(resource.getFile(), ProjetoEntity.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
