package uno.d1s.pulseq.controller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import uno.d1s.pulseq.core.constant.mapping.BadgeMappingConstants
import javax.servlet.http.HttpServletResponse
import javax.validation.constraints.NotEmpty

interface BadgeController {

    @RequestMapping(
        BadgeMappingConstants.GET_BADGE,
        method = [RequestMethod.GET]
    )
    fun getBadge(
        @PathVariable @NotEmpty statisticId: String,
        @RequestParam(required = false) color: String?,
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) style: String?,
        @RequestParam(required = false) logoUrl: String?,
        response: HttpServletResponse
    )
}