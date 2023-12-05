package studio.hcmc.ktor.serialization

import io.ktor.http.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

@ExperimentalSerializationApi
object UrlSerializer : KSerializer<Url> {
    private val pathSegmentSerializer = ListSerializer(String.serializer())

    override val descriptor = buildClassSerialDescriptor("Url") {
        element<@Serializable(with = URLProtocolSerializer::class) URLProtocol>("protocol")
        element<String>("host")
        element<Int>("specifiedPort")
        element<List<String>>("pathSegments")
        element<Parameters>("parameters")
        element<String>("fragment")
        element<String?>("user", isOptional = true)
        element<String?>("password", isOptional = true)
        element<Boolean>("trailingQuery")
    }

    override fun serialize(encoder: Encoder, value: Url) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, URLProtocolSerializer, value.protocol)
            encodeStringElement(descriptor, 1, value.host)
            encodeIntElement(descriptor, 2, value.specifiedPort)
            encodeSerializableElement(descriptor, 3, pathSegmentSerializer, value.pathSegments)
            encodeSerializableElement(descriptor, 4, ParametersSerializer, value.parameters)
            encodeStringElement(descriptor, 5, value.fragment)
            encodeNullableSerializableElement(descriptor, 6, String.serializer().nullable, value.user)
            encodeNullableSerializableElement(descriptor, 7, String.serializer().nullable, value.password)
            encodeBooleanElement(descriptor, 8, value.trailingQuery)
        }
    }

    override fun deserialize(decoder: Decoder): Url {
        var protocol: URLProtocol? = null
        var host: String? = null
        var specifiedPort: Int? = null
        var pathSegments: List<String>? = null
        var parameters: Parameters? = null
        var fragment: String? = null
        var user: String? = null
        var password: String? = null
        var trailingQuery: Boolean? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> protocol = decodeSerializableElement(descriptor, index, URLProtocolSerializer)
                    1 -> host = decodeStringElement(descriptor, index)
                    2 -> specifiedPort = decodeIntElement(descriptor, index)
                    3 -> pathSegments = decodeSerializableElement(descriptor, index, pathSegmentSerializer)
                    4 -> parameters = decodeSerializableElement(descriptor, index, ParametersSerializer)
                    5 -> fragment = decodeStringElement(descriptor, index)
                    6 -> user = decodeNullableSerializableElement(descriptor, index, String.serializer().nullable)
                    7 -> password = decodeNullableSerializableElement(descriptor, index, String.serializer().nullable)
                    8 -> trailingQuery = decodeBooleanElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    CompositeDecoder.UNKNOWN_NAME -> continue
                }
            }
        }

        return URLBuilder(
            protocol = protocol ?: throw SerializationException("`protocol` is not met."),
            host = host ?: "",
            port = specifiedPort ?: DEFAULT_PORT,
            user = user,
            password = password,
            pathSegments = pathSegments ?: emptyList(),
            parameters = parameters ?: Parameters.Empty,
            fragment = fragment ?: "",
            trailingQuery = trailingQuery ?: false
        ).build()
    }
}

object UrlAsStringSerializer : KSerializer<Url> {
    override val descriptor = PrimitiveSerialDescriptor("Url", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Url) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Url {
        return Url(decoder.decodeString())
    }
}

object URLProtocolSerializer : KSerializer<URLProtocol> {
    override val descriptor = buildClassSerialDescriptor("URLProtocol") {
        element<String>("name")
        element<Int>("defaultPort")
    }

    override fun serialize(encoder: Encoder, value: URLProtocol) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.name)
            encodeIntElement(descriptor, 1, value.defaultPort)
        }
    }

    override fun deserialize(decoder: Decoder): URLProtocol {
        var name: String? = null
        var defaultPort: Int? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> name = decodeStringElement(descriptor, index)
                    1 -> defaultPort = decodeIntElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    CompositeDecoder.UNKNOWN_NAME -> continue
                }
            }
        }

        return URLProtocol(
            name = name ?: throw SerializationException("`name` is not met."),
            defaultPort = defaultPort ?: throw SerializationException("`defaultPort` is not met.")
        )
    }
}