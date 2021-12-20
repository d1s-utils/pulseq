/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.domain

import org.hibernate.annotations.GenericGenerator
import java.time.Duration
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "beat", schema = "public")
class Beat(
    @ManyToOne
    val source: Source,

    @Column
    val inactivityBeforeBeat: Duration?, // could be null if it is a first beat.

    @Column
    val beatTime: Instant = Instant.now()
) {
    @Id
    @Column
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
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