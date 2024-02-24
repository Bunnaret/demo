package com.elastic.demo.configuration

import com.elastic.demo.utils.SwaggerConstant
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringDocConfig {

    @Bean
    fun groupedOpenApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("demo-api")
            .packagesToScan("com.elastic.demo")
            .build()
    }

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title(SwaggerConstant.TITLE)
                    .description(SwaggerConstant.DESCRIPTION)
                    .version(SwaggerConstant.VERSION)
                    .termsOfService(SwaggerConstant.TERM_OF_SERVICE_URL)
                    .license(License()
                        .name(SwaggerConstant.CONTACT_VERSION)
                        .url(SwaggerConstant.CONTACT_URL_VERSION))
                    .contact(Contact()
                        .name(SwaggerConstant.CONTACT_NAME)
                        .url(SwaggerConstant.CONTACT_URL)
                        .email(SwaggerConstant.CONTACT_EMAIL)
                    )
            )
    }
}