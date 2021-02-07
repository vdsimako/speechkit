package ru.vdsimako.speechkit.adapter

import mu.KotlinLogging
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import ru.vdsimako.speechkit.config.ApplicationProperties
import ru.vdsimako.speechkit.model.adapter.speech.BearerTokenDto
import ru.vdsimako.speechkit.model.adapter.speech.SpeechTextDto
import java.nio.file.Files
import java.nio.file.Paths
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.Invocation
import javax.ws.rs.core.MultivaluedHashMap
import javax.ws.rs.core.MultivaluedMap
import javax.xml.ws.Response


@Service
class SpeechKitAdapter(
    val restTemplate: RestTemplate,
    val asyncRestTemplate: RestTemplate,
    val applicationProperties: ApplicationProperties
) :
    ISpeechKitAdapter {

    private val log = KotlinLogging.logger {}

    companion object {
        const val TEXT_TO_AUDIO = "/tts:synthesize"
        const val AUDIO_TO_TEXT = "/stt:recognize"
    }

    override fun translateTextToAudio(lang: String, text: String): ByteArray? {
        val headers = HttpHeaders()

        headers.setBearerAuth(getBearerToken())
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        headers.accept = listOf(MediaType.ALL)

        val map: MultiValueMap<String, Any> = LinkedMultiValueMap()
        map.add("lang", lang)
        map.add("folderId", applicationProperties.folderId)
        map.add("text", text)

        val request = HttpEntity(map, headers)

        log.info { "Try call yandex speech kit api $lang" }

        val postForObject =
            restTemplate.postForObject(applicationProperties.speechUrl + TEXT_TO_AUDIO, request, ByteArray::class.java)

        log.info { "Successful cal yandex speech kit api" }

        return postForObject
    }

    override fun translateFileToAudio(
        file: ByteArray,
        lang: String,
        topic: String,
        profanityFilter: Boolean,
        format: String,
        sampleRateHertz: Long
    ): SpeechTextDto? {
        val readAllBytes = Files.readAllBytes(Paths.get("/Users/vdsimako/Downloads/speech_ya.ogg"))

        val client: Client = ClientBuilder.newClient()

        val webTarget =
            client.target("https://stt.api.cloud.yandex.net/speech/v1/stt:recognize?topic=general&folderId=${applicationProperties.folderId}")

        val map: MultivaluedMap<String, Any> = MultivaluedHashMap()
        map.add("authorization", "Bearer " + getBearerToken())
        map.add("Content-Type", "application/x-www-form-urlencoded")
        map.add("Transfer-Encoding", "chunked")

        val invocationBuilder: Invocation.Builder = webTarget
            .request(MediaType.APPLICATION_JSON_VALUE)
            .headers(map)

        val entity = Entity.entity(file, MediaType.APPLICATION_FORM_URLENCODED_VALUE)

        val post = invocationBuilder.post(entity)

        val readEntity = post.readEntity(SpeechTextDto::class.java)

        return readEntity
    }

//    override fun translateFileToAudio(
//        file: ByteArray,
//        lang: String,
//        topic: String,
//        profanityFilter: Boolean,
//        format: String,
//        sampleRateHertz: Long
//    ): SpeechTextDto? {
//        val headers = HttpHeaders()
//
//        headers.setBearerAuth(getBearerToken())
////        headers["Transfer-Encoding"] = "chunked"
//        headers.accept = listOf(MediaType.ALL)
////        headers.contentLength = file.size.toLong()
////        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
//        headers.set("Transfer-Encoding", "chunked")
//
//        val queryParams = mutableMapOf<String, Any>()
//
//        queryParams["lang"] = lang
//        queryParams["topic"] = topic
//        queryParams["profanityFilter"] = profanityFilter
//        queryParams["format"] = format
//        queryParams["sampleRateHertz"] = sampleRateHertz
//        queryParams["folderId"] = applicationProperties.folderId
//
//        val readAllBytes = Files.readAllBytes(Paths.get("/Users/vdsimako/Downloads/speech_ya.ogg"))
//
//        val request = HttpEntity(readAllBytes, headers)
//
//        log.info { "Try call yandex speech kit api to translate file to text" }
//
//        val speechTextDto = asyncRestTemplate.postForObject(
//            applicationProperties.speechUrl + AUDIO_TO_TEXT,
//            request,
//            SpeechTextDto::class.java,
//            queryParams
//        )
//
//        log.info { "Successful cal yandex speech kit api to translate file to text" }
//
//        return speechTextDto
//    }

    fun getBearerToken(): String {
        val headers = HttpHeaders()

        val payload = "{\"yandexPassportOauthToken\":\"${applicationProperties.token}\"}"

        val request = HttpEntity(payload, headers)

        log.info { "Try call yandex oauth api" }

        val bearerTokenDto =
            restTemplate.postForObject(applicationProperties.authUrl, request, BearerTokenDto::class.java)

        log.info { "Successful cal yandex oauth api" }

        return bearerTokenDto!!.iamToken
    }
}