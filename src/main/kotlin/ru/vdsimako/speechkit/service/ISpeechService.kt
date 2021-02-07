package ru.vdsimako.speechkit.service

import org.springframework.web.multipart.MultipartFile
import ru.vdsimako.speechkit.model.SpeechDto
import ru.vdsimako.speechkit.model.adapter.speech.SpeechTextDto

interface ISpeechService {

    fun translateTextToFile(speechDto: SpeechDto, lang: String): ByteArray?

    fun translateFileToAudio(
        file: MultipartFile,
        lang: String,
        topic: String,
        profanityFilter: Boolean,
        format: String,
        sampleRateHertz: Long
    ): SpeechTextDto?
}