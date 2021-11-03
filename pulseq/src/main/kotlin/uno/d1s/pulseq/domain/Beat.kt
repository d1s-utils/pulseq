package uno.d1s.pulseq.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Duration
import java.time.Instant

@Document(collection = "beat")
class Beat(
    @DBRef
    val device: Device,
    val inactivityBeforeBeat: Duration?, // could be null if it is a first beat.
    val beatTime: Instant = Instant.now()
) {
    @Id
    var id: String? = null
}