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
    @ManyToOne val source: Source,

    @ManyToOne val holder: Holder,

    @Column val inactivityBeforeBeat: Duration?, // could be null if this is a first beat.

    @Column(unique = true, nullable = false) val instant: Instant = Instant.now()
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
        if (instant != other.instant) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + (inactivityBeforeBeat?.hashCode() ?: 0)
        result = 31 * result + instant.hashCode()
        return result
    }

    override fun toString(): String {
        return "Beat(source=$source, inactivityBeforeBeat=$inactivityBeforeBeat, beatTime=$instant, id=$id)"
    }
}