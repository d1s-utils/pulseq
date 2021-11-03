package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import uno.d1s.pulseq.controller.BadgeController
import uno.d1s.pulseq.service.BadgeService

@RestController
class BadgeControllerImpl : BadgeController {

    @Autowired
    private lateinit var badgeService: BadgeService

    override fun getBadge(statisticId: String): ResponseEntity<ByteArray> =
        ResponseEntity.ok(badgeService.createBadge(statisticId))
}