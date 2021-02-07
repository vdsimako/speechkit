package ru.vdsimako.speechkit.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "speech")
data class ApplicationProperties(
    val authUrl: String,
    val token: String,
    val speechUrl: String,
    val folderId: String
)
