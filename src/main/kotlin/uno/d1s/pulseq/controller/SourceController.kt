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
import uno.d1s.pulseq.constant.mapping.SourceMappingConstants
import uno.d1s.pulseq.dto.beat.BeatDto
import uno.d1s.pulseq.dto.ErrorDto
import uno.d1s.pulseq.dto.source.SourceDto
import uno.d1s.pulseq.dto.source.SourcePatchDto
import uno.d1s.pulseq.strategy.source.SourceFindingStrategyType
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@Validated
@Tag(name = "Sources", description = "Manage registered sources.")
interface SourceController {

    @Operation(summary = "Get all registered sources.")
    @ApiResponses(
        ApiResponse(
            description = "Found sources.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = ArraySchema(schema = Schema(implementation = SourceDto::class))
            )]
        )
    )
    @GetMapping(SourceMappingConstants.GET_ALL_SOURCES)
    fun getAllSources(
        @RequestParam(required = false) @Parameter(description = "The page number.") page: Int?,
        @RequestParam(required = false) @Parameter(description = "The page size.") pageSize: Int?
    ): ResponseEntity<Page<SourceDto>>

    @Operation(summary = "Get the source by specified identify.")
    @ApiResponses(
        ApiResponse(
            description = "Found the source.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = SourceDto::class)
            )]
        ), ApiResponse(
            description = "The source was not found.", responseCode = "404", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @GetMapping(SourceMappingConstants.GET_SOURCE_BY_IDENTIFY)
    fun getSourceByIdentify(
        @PathVariable @Parameter(
            description = "Source identify associated with source to search for."
        ) @NotEmpty identify: String, @RequestParam(required = false, name = "strategy") @Parameter(
            description = "The source finding strategy to use."
        ) findingStrategy: SourceFindingStrategyType?
    ): ResponseEntity<SourceDto>


    @Operation(
        summary = "Register new source.",
        description = "This operation requires server secret to be set as a header (Authorization) or request parameter (auth)."
    )
    @ApiResponses(
        ApiResponse(
            description = "Registered the source.", responseCode = "201", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = SourceDto::class)
            )]
        ), ApiResponse(
            description = "The source with the same name already exists.", responseCode = "409", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        ), ApiResponse(
            description = "The request was not authorized.", responseCode = "403", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @PostMapping(
        SourceMappingConstants.REGISTER_SOURCE,
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @PreAuthorize("@authenticationService.authenticatedRequest")
    fun registerNewSource(
        @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "The source information for the new source to be registered.", content = [Content(
                schema = Schema(implementation = SourceDto::class)
            )]
        ) @Valid source: SourcePatchDto
    ): ResponseEntity<SourceDto>

    @Operation(summary = "Get source beats.")
    @ApiResponses(
        ApiResponse(
            description = "Found the source beats.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = ArraySchema(schema = Schema(implementation = ErrorDto::class))
            )]
        ), ApiResponse(
            description = "The source was not found.", responseCode = "404", content = [Content(
                schema = Schema(implementation = ErrorDto::class), mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        )
    )
    @GetMapping(SourceMappingConstants.GET_BEATS)
    fun getSourceBeats(
        @PathVariable @Parameter(
            description = "Source identify associated with source to search for."
        ) @NotEmpty identify: String, @RequestParam(required = false, name = "strategy") @Parameter(
            description = "The source finding strategy to use."
        ) findingStrategy: SourceFindingStrategyType?,
        @RequestParam(required = false) @Parameter(description = "The page number.") page: Int?,
        @RequestParam(required = false) @Parameter(description = "The page size.") pageSize: Int?
    ): ResponseEntity<Page<BeatDto>>

    @Operation(
        summary = "Edit the source.",
        description = "This operation requires server secret to be set as a header (Authorization) or request parameter (auth)."
    )
    @ApiResponses(
        ApiResponse(
            description = "Edited the source",
            responseCode = "202",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]
        ), ApiResponse(
            description = "The source was not found.", responseCode = "404", content = [Content(
                schema = Schema(implementation = ErrorDto::class), mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        ), ApiResponse(
            description = "The source with the same name already exists.", responseCode = "409", content = [Content(
                schema = Schema(implementation = ErrorDto::class), mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        ), ApiResponse(
            description = "The request was not authorized.", responseCode = "403", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @PreAuthorize("@authenticationService.authenticatedRequest")
    @PatchMapping(SourceMappingConstants.GET_SOURCE_BY_IDENTIFY)
    fun patchSource(
        @PathVariable @Parameter(
            description = "Source identify associated with source to edit for."
        ) @NotEmpty identify: String,
        @RequestParam(required = false, name = "strategy") @Parameter(
            description = "The source finding strategy to use."
        ) findingStrategy: SourceFindingStrategyType?,
        @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The source information for the new source to be edited.") @Valid patch: SourcePatchDto
    ): ResponseEntity<SourceDto>

    @Operation(
        summary = "Delete the source.",
        description = "Note: this operation will delete all the beats associated with the source to be deleted. This operation requires server secret to be set as a header (Authorization) or request parameter (auth)."
    )
    @ApiResponses(
        ApiResponse(
            description = "Deleted the source.", responseCode = "200"
        ), ApiResponse(
            description = "The source was not found.", responseCode = "404", content = [Content(
                schema = Schema(implementation = ErrorDto::class), mediaType = MediaType.APPLICATION_JSON_VALUE
            )]
        ), ApiResponse(
            description = "The request was not authorized.", responseCode = "403", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @PreAuthorize("@authenticationService.authenticatedRequest")
    @DeleteMapping(SourceMappingConstants.GET_SOURCE_BY_IDENTIFY)
    fun deleteSource(
        @PathVariable @Parameter(
            description = "Source identify associated with source to delete for."
        ) @NotEmpty identify: String, @RequestParam(required = false, name = "strategy") @Parameter(
            description = "The source finding strategy to use."
        ) findingStrategy: SourceFindingStrategyType?
    ): ResponseEntity<Any>
}