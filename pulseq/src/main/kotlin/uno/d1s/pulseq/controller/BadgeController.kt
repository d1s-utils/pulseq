package uno.d1s.pulseq.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import uno.d1s.pulseq.constant.mapping.BadgeMappingConstants
import javax.servlet.http.HttpServletResponse

interface BadgeController {

    @GetMapping(BadgeMappingConstants.GET_BADGE)
    fun getBadge(@PathVariable statisticId: String, response: HttpServletResponse)
}