package de.muenchen.dave.geodataeai.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI(@Value("${info.application.version}") final String buildVersion) {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("DAVE GEODATA EAI")
                                .version(buildVersion)
                                .description("DAVE GEODATA EAI - Integrationsbaustein zum Abfragen von Geoinformationen"));
    }

    @Bean
    public String[] whitelist() {
        return new String[] {
                // -- swagger ui
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**",
        };
    }
}
