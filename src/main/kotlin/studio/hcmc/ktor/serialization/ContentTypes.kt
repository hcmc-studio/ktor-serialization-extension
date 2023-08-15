package studio.hcmc.ktor.serialization

import io.ktor.http.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun ContentType.toJsonObject(): JsonObject {
    return JsonObject(mapOf(
        "contentType" to JsonPrimitive(contentType),
        "contentSubtype" to JsonPrimitive(contentSubtype),
        "parameters" to JsonArray(parameters.map { it.toJsonObject() })
    ))
}