package com.patika.paycore.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI creditOpenAPI() {
        return new OpenAPI().info(new Info().title("Credit Application API").description("Paycore Project")
                .version("v0.0.1").license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation().description("Ufuk Bayraktar")
                        .url("https://github.com/ufukbayraktar"));
    }
}
