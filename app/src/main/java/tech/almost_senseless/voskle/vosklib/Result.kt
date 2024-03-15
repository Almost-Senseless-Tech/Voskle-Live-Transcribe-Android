package tech.almost_senseless.voskle.vosklib

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("spk")
    val speakerFingerprint: DoubleArray? = null,
    @SerialName("spk_frames")
    val speakerDataLength: Int? = null,
    val text: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Result

        if (speakerFingerprint != null) {
            if (other.speakerFingerprint == null) return false
            if (!speakerFingerprint.contentEquals(other.speakerFingerprint)) return false
        } else if (other.speakerFingerprint != null) return false
        if (speakerDataLength != other.speakerDataLength) return false
        return text == other.text
    }

    override fun hashCode(): Int {
        var result = speakerFingerprint?.contentHashCode() ?: 0
        result = 31 * result + (speakerDataLength ?: 0)
        result = 31 * result + text.hashCode()
        return result
    }
}