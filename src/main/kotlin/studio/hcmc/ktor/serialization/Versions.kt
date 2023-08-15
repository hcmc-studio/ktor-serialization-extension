package studio.hcmc.ktor.serialization

import io.ktor.http.content.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun Version.toJsonObject(): JsonObject {
    when (this) {
        is LastModifiedVersion -> return toJsonObject()
        is EntityTagVersion -> return toJsonObject()
        else -> return JsonObject(mapOf(
            "type" to JsonPrimitive(this::class.qualifiedName),
            "error" to JsonPrimitive("Unknown type")
        ))
    }
}

fun LastModifiedVersion.toJsonObject(): JsonObject {
    return JsonObject(mapOf(
        "lastModified" to lastModified.toJsonObject()
    ))
}

fun EntityTagVersion.toJsonObject(): JsonObject {
    return JsonObject(mapOf(
        "etag" to JsonPrimitive(etag),
        "weak" to JsonPrimitive(weak)
    ))
}