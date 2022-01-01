/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uno.d1s.pulseq.domain.Beat
import java.util.*

@Repository
interface BeatRepository : JpaRepository<Beat, String> {
    fun findBeatsByIdIn(ids: List<String>): Optional<List<Beat>>
}