package studio.hcmc.ktor.serialization

import io.ktor.util.date.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

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