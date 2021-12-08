package uno.d1s.pulseq.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import uno.d1s.pulseq.core.constant.mapping.BadgeMappingConstants
import javax.servlet.http.HttpServletResponse
import javax.validation.constraints.NotEmpty

@Validated
interface BadgeController {

    @GetMapping(BadgeMappingConstants.GET_BADGE)
    fun getBadge(
        @PathVariable @NotEmpty metricId: String,
        @RequestParam(required = false) color: String?,
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) style: String?,
        @RequestParam(required = false) logoUrl: String?,
        response: HttpServletResponse
    )
}