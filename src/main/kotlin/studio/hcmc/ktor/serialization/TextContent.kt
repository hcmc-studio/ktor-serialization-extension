package studio.hcmc.ktor.serialization

import io.ktor.http.content.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun TextContent.toJsonObject(): JsonObject {
    return JsonObject(mapOf(
        "text" to JsonPrimitive(text),
        "contentType" to contentType.toJsonObject(),
        "status" to status.toJsonElement(),
        "contentLength" to JsonPrimitive(contentLength),
        "headers" to headers.toJsonObject(),
        "caching" to caching.toJsonElement(),
        "versions" to JsonArray(versions.map(Version::toJsonObject))
    ))
}