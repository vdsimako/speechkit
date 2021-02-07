package ru.vdsimako.speechkit.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ru.vdsimako.speechkit.adapter.ISpeechKitAdapter
import ru.vdsimako.speechkit.model.SpeechDto
import ru.vdsimako.speechkit.model.adapter.speech.SpeechTextDto


@Service
class SpeechService(var speechKitAdapter: ISpeechKitAdapter) : ISpeechService {

    override fun translateTextToFile(speechDto: SpeechDto, lang: String): ByteArray? {

        val audioFile = speechKitAdapter.translateTextToAudio(lang = lang, text = speechDto.text);

        return audioFile
    }

    override fun translateFileToAudio(
        file: MultipartFile,
        lang: String,
        topic: String,
        profanityFilter: Boolean,
        format: String,
        sampleRateHertz: Long
    ): SpeechTextDto? {

        val textDto = speechKitAdapter.translateFileToAudio(file.bytes, lang, topic, profanityFilter, format, sampleRateHertz)

        return textDto
    }
}