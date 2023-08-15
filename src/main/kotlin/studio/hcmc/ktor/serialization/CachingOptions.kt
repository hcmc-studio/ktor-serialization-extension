package studio.hcmc.ktor.serialization

import io.ktor.http.content.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject

fun CachingOptions.toJsonObject(): JsonObject {
    return JsonObject(mapOf(
        "cacheControl" to cacheControl.toJsonElement(),
        "expires" to expires.toJsonElement()
    ))
}

fun CachingOptions?.toJsonElement(): JsonElement {
    return this?.toJsonObject() ?: JsonNull
}