package uno.d1s.pulseq.domain

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "source")
class Source(
    val name: String
) {
    @Id
    var id: String? = null

    @OneToMany(mappedBy = "source")
    var beats: List<Beat>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Source

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "Source(name='$name', id=$id)"
    }
}