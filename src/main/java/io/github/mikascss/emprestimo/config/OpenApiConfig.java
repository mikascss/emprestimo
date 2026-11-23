package io.github.mikascss.emprestimo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI emprestimoOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API de Empréstimo de Equipamentos")
                .version("0.0.1-SNAPSHOT")
                .description("Cadastro de equipamentos e colaboradores, controle de empréstimos, "
                        + "devoluções e consulta de atrasos. Os prazos são definidos pelo tipo do equipamento."));
    }
}
