/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "holder", schema = "public")
class Holder(
    @Column(unique = true, nullable = false) val name: String
) {
    @Id
    @Column
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    var id: String? = null

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "holder")
    var sources: List<Source>? = null

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "holder")
    var beats: List<Beat>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Holder

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "Holder(name='$name', id=$id)"
    }
}