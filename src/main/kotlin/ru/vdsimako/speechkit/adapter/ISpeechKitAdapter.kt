package ru.vdsimako.speechkit.adapter

import ru.vdsimako.speechkit.model.adapter.speech.SpeechTextDto

interface ISpeechKitAdapter {

    fun translateTextToAudio(lang: String, text: String): ByteArray?

    fun translateFileToAudio(
        file: ByteArray,
        lang: String,
        topic: String,
        profanityFilter: Boolean,
        format: String,
        sampleRateHertz: Long
    ): SpeechTextDto?
}