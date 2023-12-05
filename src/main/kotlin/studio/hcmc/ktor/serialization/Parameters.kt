package studio.hcmc.ktor.serialization

import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject
import studio.hcmc.kotlin.serialization.toJsonArray

fun Parameters.toJsonObject(): JsonObject {
    return JsonObject(toMap().mapValues { (_, values) -> values.toJsonArray() })
}