package uno.d1s.pulseq.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import uno.d1s.pulseq.configuration.property.GlobalConfigurationProperties
import uno.d1s.pulseq.core.constant.mapping.InfoMappingConstants

interface InfoController {

    @GetMapping(InfoMappingConstants.BASE)
    fun getAllInformation(): ResponseEntity<GlobalConfigurationProperties>
}