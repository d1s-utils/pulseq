package uno.d1s.pulseq.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Duration
import java.time.Instant

@Document(collection = "beat")
class Beat(
    @DBRef
    val source: Source,
    val inactivityBeforeBeat: Duration?, // could be null if it is a first beat.
    val beatTime: Instant = Instant.now()
) {
    @Id
    var id: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Beat

        if (source != other.source) return false
        if (inactivityBeforeBeat != other.inactivityBeforeBeat) return false
        if (beatTime != other.beatTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + (inactivityBeforeBeat?.hashCode() ?: 0)
        result = 31 * result + beatTime.hashCode()
        return result
    }

    override fun toString(): String {
        return "Beat(source=$source, inactivityBeforeBeat=$inactivityBeforeBeat, beatTime=$beatTime, id=$id)"
    }
}