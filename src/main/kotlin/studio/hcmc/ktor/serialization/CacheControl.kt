package studio.hcmc.ktor.serialization

import io.ktor.http.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun CacheControl.toJsonObject(): JsonObject {
    return JsonObject(mapOf(
        "visibility" to visibility.toJsonElement()
    ))
}

fun CacheControl?.toJsonElement(): JsonElement {
    return this?.toJsonObject() ?: JsonNull
}

fun CacheControl.Visibility.toJsonObject(): JsonObject {
    return JsonObject(mapOf(
        "name" to JsonPrimitive(name),
        "ordinal" to JsonPrimitive(ordinal),
    ))
}

fun CacheControl.Visibility?.toJsonElement(): JsonElement {
    return this?.toJsonObject() ?: JsonNull
}