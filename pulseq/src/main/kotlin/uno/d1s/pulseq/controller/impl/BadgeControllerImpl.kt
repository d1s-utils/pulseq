package uno.d1s.pulseq.service.impl

import org.springframework.http.ResponseEntity
import uno.d1s.pulseq.controller.BadgeController

class BadgeControllerImpl : BadgeController {
    override fun getBadge(statisticId: String): ResponseEntity<Array<Byte>> {
        TODO("Not yet implemented")
    }
}