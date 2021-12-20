package uno.d1s.pulseq.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uno.d1s.pulseq.domain.Beat

@Repository
interface BeatRepository : JpaRepository<Beat, String>