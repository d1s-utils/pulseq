package uno.d1s.pulseq.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import uno.d1s.pulseq.constant.mapping.BadgeMappingConstants
import uno.d1s.pulseq.dto.ErrorDto
import javax.servlet.http.HttpServletResponse
import javax.validation.constraints.NotEmpty

@Validated
@Tag(name = "Badges", description = "Get badges based on pulseq metrics.")
interface BadgeController {

    @Operation(summary = "Get the badge.")
    @ApiResponses(
        ApiResponse(
            description = "The badge", responseCode = "200", content = [Content(
                mediaType = "image/svg+xml"
            )]
        ), ApiResponse(
            description = "If the metric associated with the given id was not found.",
            responseCode = "404",
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        ), ApiResponse(
            description = "If the provided logo is invalid or too large.", responseCode = "422", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @GetMapping(BadgeMappingConstants.GET_BADGE)
    fun getBadge(
        @PathVariable @NotEmpty metricId: String,
        @RequestParam(required = false) @Parameter(description = "Override the badge color.") color: String?,
        @RequestParam(required = false) @Parameter(description = "Override the badge title.") title: String?,
        @RequestParam(required = false) @Parameter(description = "Override the badge style.") style: String?,
        @RequestParam(required = false) @Parameter(description = "Set the logo for the badge.") logoUrl: String?,
        response: HttpServletResponse
    )
}