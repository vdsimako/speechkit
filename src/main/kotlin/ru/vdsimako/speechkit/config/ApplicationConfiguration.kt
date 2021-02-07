package ru.vdsimako.speechkit.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate


@Configuration
class ApplicationConfiguration(val applicationProperties: ApplicationProperties) {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate? {
        val restTemplate = builder
            .build()

//        val httpComponentsClientHttpRequestFactory = HttpComponentsClientHttpRequestFactory()

//        restTemplate.requestFactory = httpComponentsClientHttpRequestFactory

        return restTemplate
    }

    @Bean
    fun folderId(): String {
        return applicationProperties.folderId
    }

    @Bean
    fun authUrl(): String {
        return applicationProperties.authUrl
    }

    @Bean
    fun token(): String {
        return applicationProperties.token
    }

    @Bean
    fun speechUrl(): String {
        return applicationProperties.speechUrl
    }


}
