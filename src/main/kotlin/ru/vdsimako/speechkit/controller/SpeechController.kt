package ru.vdsimako.speechkit.controller

import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.vdsimako.speechkit.model.SpeechDto
import ru.vdsimako.speechkit.model.adapter.speech.SpeechTextDto
import ru.vdsimako.speechkit.service.ISpeechService

@RestController
class SpeechController(val speechService: ISpeechService) {

    private val log = KotlinLogging.logger {}

    @PostMapping(
        "/translate",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun translateTextToAudio(
        @RequestParam(value = "lang", defaultValue = "en-US") lang: String,
        @RequestBody speechDto: SpeechDto
    ): ResponseEntity<ByteArray> {
        log.info { "Get request for translate text into audio" }

        val response = speechService.translateTextToFile(speechDto, lang)

        log.info { "Return response for translate text into audio" }

        return ResponseEntity.ok()
            .header("content-type", "audio/opus")
            .header("content-disposition", "inline")
            .body(response)
    }

    @PostMapping(
        "/translate",
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun translateAudioToText(
        @RequestParam(value = "lang", defaultValue = "en-US") lang: String,
        @RequestParam(value = "topic", defaultValue = "general") topic: String,
        @RequestParam(value = "profanityFilter", defaultValue = "false") isProfanityFilter: Boolean,
        @RequestParam(value = "format", defaultValue = "oggopus") format: String,
        @RequestParam(value = "sampleRateHertz", defaultValue = "48000") sampleRateHertz: Long,
        @RequestBody file: MultipartFile
    ): ResponseEntity<SpeechTextDto> {

        log.info { "Get request for translate audio into text" }

        val response = speechService.translateFileToAudio(file, lang, topic, isProfanityFilter, format, sampleRateHertz)

        log.info { "Return response for translate audio into text" }

        return ResponseEntity
            .ok()
            .body(response)
    }

}