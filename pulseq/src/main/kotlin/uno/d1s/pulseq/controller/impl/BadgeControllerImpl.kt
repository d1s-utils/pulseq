package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uno.d1s.pulseq.controller.BadgeController
import uno.d1s.pulseq.service.BadgeService
import javax.servlet.http.HttpServletResponse

@RestController
class BadgeControllerImpl : BadgeController {

    @Autowired
    private lateinit var badgeService: BadgeService

    override fun getBadge(@PathVariable statisticId: String, response: HttpServletResponse) {
        response.run {
            contentType = "image/svg+xml"
            writer.println(String(badgeService.createBadge(statisticId)))
        }
    }
}