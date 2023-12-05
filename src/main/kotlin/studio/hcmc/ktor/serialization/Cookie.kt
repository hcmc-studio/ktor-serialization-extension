package studio.hcmc.ktor.serialization

import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

object CookieEncodingSerializer : KSerializer<CookieEncoding> {
    override val descriptor = PrimitiveSerialDescriptor("CookieEncoding", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): CookieEncoding {
        return CookieEncoding.valueOf(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: CookieEncoding) {
        encoder.encodeString(value.name)
    }
}

@ExperimentalSerializationApi
object CookieSerializer : KSerializer<Cookie> {
    private val extensionsSerializer = MapSerializer(String.serializer(), String.serializer().nullable)

    override val descriptor = buildClassSerialDescriptor("Cookie") {
        element<String>("name")
        element<String>("value")
        element<CookieEncoding>("encoding")
        element<Int>("maxAge")
        element<@Serializable(with = GMTDateAsLongSerializer::class) GMTDate?>("expires", isOptional = true)
        element<String?>("domain", isOptional = true)
        element<String?>("path", isOptional = true)
        element<Boolean>("secure")
        element<Boolean>("httpOnly")
        element<Map<String, String?>>("extensions")
    }

    override fun deserialize(decoder: Decoder): Cookie {
        var name: String? = null
        var value: String? = null
        var encoding: CookieEncoding? = null
        var maxAge: Int? = null
        var expires: GMTDate? = null
        var domain: String? = null
        var path: String? = null
        var secure: Boolean? = null
        var httpOnly: Boolean? = null
        var extensions: Map<String, String?>? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> name = decodeStringElement(descriptor, index)
                    1 -> value = decodeStringElement(descriptor, index)
                    2 -> encoding = decodeNullableSerializableElement(descriptor, index, CookieEncodingSerializer)
                    3 -> maxAge = decodeIntElement(descriptor, index)
                    4 -> expires = decodeNullableSerializableElement(descriptor, index, GMTDateAsLongSerializer)
                    5 -> domain = decodeNullableSerializableElement(descriptor, index, String.serializer().nullable)
                    6 -> path = decodeNullableSerializableElement(descriptor, index, String.serializer().nullable)
                    7 -> secure = decodeBooleanElement(descriptor, index)
                    8 -> httpOnly = decodeBooleanElement(descriptor, index)
                    9 -> extensions = decodeNullableSerializableElement(descriptor, index, extensionsSerializer)
                    CompositeDecoder.DECODE_DONE -> break
                    CompositeDecoder.UNKNOWN_NAME -> continue
                }
            }
        }

        return Cookie(
            name = name ?: throw SerializationException("`name` is not met."),
            value = value ?: throw SerializationException("`value` is not met."),
            encoding = encoding ?: CookieEncoding.URI_ENCODING,
            maxAge = maxAge ?: 0,
            expires = expires,
            domain = domain,
            path = path,
            secure = secure ?: false,
            httpOnly = httpOnly ?: false,
            extensions = extensions ?: emptyMap()
        )
    }

    override fun serialize(encoder: Encoder, value: Cookie) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.name)
            encodeStringElement(descriptor, 1, value.value)
            encodeSerializableElement(descriptor, 2, CookieEncodingSerializer, value.encoding)
            encodeIntElement(descriptor, 3, value.maxAge)
            encodeNullableSerializableElement(descriptor, 4, GMTDateAsLongSerializer, value.expires)
            encodeNullableSerializableElement(descriptor, 5, String.serializer().nullable, value.domain)
            encodeNullableSerializableElement(descriptor, 6, String.serializer().nullable, value.path)
            encodeBooleanElement(descriptor, 7, value.secure)
            encodeBooleanElement(descriptor, 8, value.httpOnly)
            encodeSerializableElement(descriptor, 9, extensionsSerializer, value.extensions)
        }
    }
}