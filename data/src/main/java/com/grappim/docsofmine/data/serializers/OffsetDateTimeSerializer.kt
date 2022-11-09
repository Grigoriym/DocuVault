package com.grappim.docsofmine.data.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object OffsetDateTimeSerializer : KSerializer<OffsetDateTime> {

    override fun deserialize(decoder: Decoder): OffsetDateTime {
        val millis = decoder.decodeString()
        return OffsetDateTime.from(
            DateTimeFormatter
                .ISO_OFFSET_DATE_TIME
                .parse(millis)
        )
    }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("date", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: OffsetDateTime) {
        encoder.encodeString(
            value.format(
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
            )
        )
    }
}