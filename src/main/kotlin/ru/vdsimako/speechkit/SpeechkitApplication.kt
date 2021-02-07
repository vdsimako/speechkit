package ru.vdsimako.speechkit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.vdsimako.speechkit.config.ApplicationProperties

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class SpeechkitApplication

fun main(args: Array<String>) {
    runApplication<SpeechkitApplication>(*args)
}
