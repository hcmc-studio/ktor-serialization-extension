package studio.hcmc.ktor.serialization

import io.ktor.http.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun HeaderValueParam.toJsonObject(): JsonObject {
    return JsonObject(mapOf(
        "name" to JsonPrimitive(name),
        "value" to JsonPrimitive(value),
        "escapeValue" to JsonPrimitive(escapeValue)
    ))
}