package studio.hcmc.ktor.serialization

import io.ktor.http.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ParametersSerializer : KSerializer<Parameters> {
    override val descriptor = StringValuesSerializer.descriptor

    override fun serialize(encoder: Encoder, value: Parameters) {
        StringValuesSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Parameters {
        val stringValues = StringValuesSerializer.deserialize(decoder)
        return Parameters.build {
            appendAll(stringValues)
        }
    }
}