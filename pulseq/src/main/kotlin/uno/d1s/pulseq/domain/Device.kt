package uno.d1s.pulseq.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Device(
    val name: String
) {
    @Id
    var id: String? = null

    @DBRef
    var beats: List<Beat>? = null
}