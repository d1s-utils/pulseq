/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import uno.d1s.pulseq.configuration.property.GlobalConfigurationProperties
import uno.d1s.pulseq.controller.InfoController

@RestController
class InfoControllerImpl : InfoController {

    @Autowired
    private lateinit var globalConfigurationProperties: GlobalConfigurationProperties

    override fun getAllInformation(): ResponseEntity<GlobalConfigurationProperties> =
        ResponseEntity.ok(globalConfigurationProperties)
}