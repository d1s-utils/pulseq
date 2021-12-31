/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.dto.source

class SourceDto(
    val sourceName: String
) {
    var id: String? = null
    var beats: List<String>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SourceDto

        if (sourceName != other.sourceName) return false
        if (beats != other.beats) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sourceName.hashCode()
        result = 31 * result + (beats?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "SourceDto(sourceName='$sourceName', id=$id, beats=$beats)"
    }
}