package studio.hcmc.ktor.serialization

import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.json.JsonObject
import studio.hcmc.kotlin.serialization.toJsonArray

fun Parameters.toJsonObject(): JsonObject {
    return JsonObject(toMap().mapValues { (_, values) -> values.toJsonArray() })
}