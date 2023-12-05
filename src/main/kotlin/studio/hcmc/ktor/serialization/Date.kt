package studio.hcmc.ktor.serialization

import io.ktor.util.date.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

object GMTDateSerializer : KSerializer<GMTDate> {
    override val descriptor = buildClassSerialDescriptor("GMTDate") {
        element<Int>("seconds")
        element<Int>("minutes")
        element<Int>("hours")
        element<Int>("dayOfMonth")
        element<Month>("month")
        element<Int>("year")
    }

    override fun serialize(encoder: Encoder, value: GMTDate) {
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.seconds)
            encodeIntElement(descriptor, 1, value.minutes)
            encodeIntElement(descriptor, 2, value.hours)
            encodeIntElement(descriptor, 3, value.dayOfMonth)
            encodeSerializableElement(descriptor, 4, MonthSerializer, value.month)
            encodeIntElement(descriptor, 5, value.year)
        }
    }

    override fun deserialize(decoder: Decoder): GMTDate {
        var seconds = 0
        var minutes = 0
        var hours = 0
        var dayOfMonth = 0
        var month = Month.JANUARY
        var year = 0
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> seconds = decodeIntElement(descriptor, index)
                    1 -> minutes = decodeIntElement(descriptor, index)
                    2 -> hours = decodeIntElement(descriptor, index)
                    3 -> dayOfMonth = decodeIntElement(descriptor, index)
                    4 -> month = decodeSerializableElement(descriptor, index, MonthSerializer)
                    5 -> year = decodeIntElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    CompositeDecoder.UNKNOWN_NAME -> continue
                }
            }
        }

        return GMTDate(seconds, minutes, hours, dayOfMonth, month, year)
    }
}

object GMTDateAsLongSerializer : KSerializer<GMTDate> {
    override val descriptor = PrimitiveSerialDescriptor("GMTDate", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: GMTDate) {
        encoder.encodeLong(value.timestamp)
    }

    override fun deserialize(decoder: Decoder): GMTDate {
        return GMTDate(decoder.decodeLong())
    }
}

object MonthSerializer : KSerializer<Month> {
    override val descriptor = PrimitiveSerialDescriptor("Month", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Month) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): Month {
        return Month.from(decoder.decodeString())
    }
}

fun GMTDate.toJsonObject(): JsonObject {
    return JsonObject(mapOf(
        "seconds" to JsonPrimitive(seconds),
        "minutes" to JsonPrimitive(minutes),
        "hours" to JsonPrimitive(hours),
        "dayOfWeek" to dayOfWeek.toJsonObject(),
        "dayOfMonth" to JsonPrimitive(dayOfMonth),
        "dayOfYear" to JsonPrimitive(dayOfYear),
        "month" to month.toJsonObject(),
        "year" to JsonPrimitive(year),
        "timestamp" to JsonPrimitive(timestamp)
    ))
}

fun GMTDate?.toJsonElement(): JsonElement {
    return this?.toJsonObject() ?: JsonNull
}

fun WeekDay.toJsonObject(): JsonObject {
    return JsonObject(mapOf(
        "name" to JsonPrimitive(name),
        "ordinal" to JsonPrimitive(ordinal),
        "value" to JsonPrimitive(value)
    ))
}

fun Month.toJsonObject(): JsonObject {
    return JsonObject(mapOf(
        "name" to JsonPrimitive(name),
        "ordinal" to JsonPrimitive(ordinal),
        "value" to JsonPrimitive(value)
    ))
}