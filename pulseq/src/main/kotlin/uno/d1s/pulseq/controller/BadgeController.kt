package uno.d1s.pulseq.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import uno.d1s.pulseq.constant.mapping.BadgeMappingConstants

interface BadgeController {

    @GetMapping(BadgeMappingConstants.GET_BADGE, produces = [MediaType.IMAGE_PNG_VALUE])
    fun getBadge(@PathVariable statisticId: String): ResponseEntity<ByteArray>
}