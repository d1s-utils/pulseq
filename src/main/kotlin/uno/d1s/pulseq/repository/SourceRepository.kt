package uno.d1s.pulseq.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uno.d1s.pulseq.domain.Source
import java.util.*

@Repository
interface SourceRepository : JpaRepository<Source, String> {

    fun findSourceByNameEqualsIgnoreCase(sourceName: String): Optional<Source>
}