package ru.linkshare.domain.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@JvmInline
@Serializable(UUIDSerializer::class)
value class UID(val uid: UUID)

class UUIDSerializer : KSerializer<UID> {
    override val descriptor = PrimitiveSerialDescriptor("UID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UID {
        return UID(UUID.fromString(decoder.decodeString()))
    }

    override fun serialize(encoder: Encoder, value: UID) {
        encoder.encodeString(value.uid.toString())
    }
}