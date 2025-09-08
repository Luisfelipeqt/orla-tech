package br.com.orla.tech.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.util.TimeZone.getTimeZone;
import static java.util.TimeZone.setDefault;

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        setDefault(getTimeZone("America/Sao_Paulo"));
        SpringApplication.run(ApiApplication.class, args);
    }
}