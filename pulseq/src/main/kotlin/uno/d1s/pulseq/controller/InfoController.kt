package uno.d1s.pulseq.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import uno.d1s.pulseq.configuration.property.GlobalConfigurationProperties
import uno.d1s.pulseq.core.constant.mapping.InfoMappingConstants

@Tag(name = "Server information.", description = "Get info about the user's server.")
interface InfoController {

    @Operation(summary = "Get all information.")
    @ApiResponses(
        ApiResponse(
            description = "The server info.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(implementation = GlobalConfigurationProperties::class)
            )]
        )
    )
    @GetMapping(InfoMappingConstants.BASE)
    fun getAllInformation(): ResponseEntity<GlobalConfigurationProperties>
}