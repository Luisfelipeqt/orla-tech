package br.com.orla.tech.api.commons.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WrapperDTO<T> {
    private List<T> content;
}