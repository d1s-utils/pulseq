/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import uno.d1s.pulseq.constant.mapping.BeatMappingConstants
import uno.d1s.pulseq.dto.beat.BeatDto
import uno.d1s.pulseq.dto.ErrorDto
import javax.servlet.http.HttpServletResponse
import javax.validation.constraints.NotEmpty

@Validated
@Tag(name = "Beats", description = "Get data about received beats and manage them.")
interface BeatController {

    @Operation(summary = "Get beat by id.")
    @ApiResponses(
        ApiResponse(
            description = "Found the beat.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = BeatDto::class)
            )]
        ), ApiResponse(
            description = "The beat was not found.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @GetMapping(BeatMappingConstants.GET_BEAT_BY_ID)
    fun getBeatBtId(@PathVariable @Parameter(description = "The ID of the beat to be found.") @NotEmpty id: String): ResponseEntity<BeatDto>

    @Operation(
        summary = "Register new beat.",
        description = "Note: A new source will be created if the provided one was not found. You can use both - source ID and name. This operation requires server secret to be set as a header (Authorization) or request parameter (auth)."
    )
    @ApiResponses(
        ApiResponse(
            description = "Registered the beat.", responseCode = "201", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = BeatDto::class)
            )]
        ), ApiResponse(
            description = "The request was not authorized.", responseCode = "403", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @PostMapping(BeatMappingConstants.BASE)
    @PreAuthorize("@authenticationService.authenticatedRequest")
    fun registerNewBeatWithSourceIdentify(
        @RequestParam(
            "sourceParam", required = false
        ) @Parameter(description = "The source ID or name.") sourceParam: String?, @RequestHeader(
            "Source", required = false
        ) @Parameter(description = "The source ID or name.") sourceHeader: String?, response: HttpServletResponse
    ): ResponseEntity<BeatDto>?

    @Operation(summary = "Get all beats.")
    @ApiResponses(
        ApiResponse(
            description = "Found beats.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = ArraySchema(schema = Schema(implementation = BeatDto::class))
            )]
        )
    )
    @GetMapping(BeatMappingConstants.GET_BEATS)
    fun getBeats(
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) pageSize: Int?
    ): ResponseEntity<Page<BeatDto>>

    @Operation(summary = "Get last received beat.")
    @ApiResponses(
        ApiResponse(
            description = "Found last beat.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = BeatDto::class)
            )]
        ), ApiResponse(
            description = "No beats received yet.", responseCode = "422", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @GetMapping(BeatMappingConstants.LAST_BEAT)
    fun getLastBeat(): ResponseEntity<BeatDto>

    @Operation(
        summary = "Delete the beat.",
        description = "This operation requires server secret to be set as a header (Authorization) or request parameter (auth) as a header (Authorization) or request parameter (auth)."
    )
    @ApiResponses(
        ApiResponse(
            description = "Deleted the beat.", responseCode = "204"
        ), ApiResponse(
            description = "The request was not authorized.", responseCode = "403", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @DeleteMapping(BeatMappingConstants.GET_BEAT_BY_ID)
    @PreAuthorize("@authenticationService.authenticatedRequest")
    fun deleteBeat(@PathVariable @Parameter(description = "The beat ID.") @NotEmpty id: String): ResponseEntity<Any>
}