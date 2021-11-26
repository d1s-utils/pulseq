package uno.d1s.pulseq

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.BeatDto
import java.time.Instant

const val VALID_ID = "valid_id"
const val INVALID_ID = "invalid_id"

val testBeat = Beat(Device("name"), null)
val testBeatDto = BeatDto("name", Instant.now(), null)
