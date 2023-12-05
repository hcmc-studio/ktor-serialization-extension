package studio.hcmc.ktor.serialization

import io.ktor.util.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import kotlinx.serialization.encoding.Encoder

object StringValuesSerializer : KSerializer<StringValues> {
    private val entriesSerializer = MapSerializer(String.serializer(), ListSerializer(String.serializer()))
    override val descriptor = buildClassSerialDescriptor("StringValues") {
        element<Boolean>("caseInsensitiveName")
        element<Map<String, List<String>>>("entries")
    }

    override fun serialize(encoder: Encoder, value: StringValues) {
        encoder.encodeStructure(descriptor) {
            encodeBooleanElement(descriptor, 0, value.caseInsensitiveName)
            encodeSerializableElement(descriptor, 1, entriesSerializer, value.toMap())
        }
    }

    override fun deserialize(decoder: Decoder): StringValues {
        var caseInsensitiveName = false
        var entries: Map<String, List<String>> = emptyMap()
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> caseInsensitiveName = decodeBooleanElement(descriptor, index)
                    1 -> entries = decodeSerializableElement(descriptor, index, entriesSerializer)
                    CompositeDecoder.DECODE_DONE -> break
                    CompositeDecoder.UNKNOWN_NAME -> continue
                }
            }
        }

        return StringValues.build(caseInsensitiveName) {
            for ((name, values) in entries) {
                appendAll(name, values)
            }
        }
    }
}