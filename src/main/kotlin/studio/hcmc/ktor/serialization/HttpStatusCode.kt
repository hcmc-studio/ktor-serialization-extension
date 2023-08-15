package studio.hcmc.ktor.serialization

import io.ktor.http.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun HttpStatusCode.toJsonObject(): JsonObject {
    return JsonObject(mapOf(
        "value" to JsonPrimitive(value),
        "description" to JsonPrimitive(description)
    ))
}

fun HttpStatusCode?.toJsonElement(): JsonElement {
    return this?.toJsonObject() ?: JsonNull
}