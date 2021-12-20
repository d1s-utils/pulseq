package uno.d1s.pulseq.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "source", schema = "public")
class Source(
    val name: String
) {
    @Id
    @Column
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    var id: String? = null

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "source")
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