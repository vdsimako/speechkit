package ru.vdsimako.speechkit.model.adapter.speech

import java.time.Instant

data class BearerTokenDto(val iamToken: String, val expiresAt: Instant)
