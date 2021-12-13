package uno.d1s.pulseq.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import uno.d1s.pulseq.constant.mapping.ActivityMappingConstants
import uno.d1s.pulseq.domain.activity.TimeSpanType
import uno.d1s.pulseq.dto.ErrorDto
import uno.d1s.pulseq.dto.TimeSpanDto

@Tag(name = "Time spans", description = "Get data about user's activity.")
interface ActivityController {

    @Operation(summary = "Get all time spans.")
    @ApiResponses(
        ApiResponse(
            description = "Calculated time spans.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = ArraySchema(schema = Schema(implementation = TimeSpanDto::class))
            )]
        ), ApiResponse(
            description = "If includeCurrent is exists, set to true and there are no available time spans yet.",
            responseCode = "422",
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )

    )
    @GetMapping(ActivityMappingConstants.GET_TIMESPANS)
    fun getAllTimeSpans(@RequestParam(required = false) @Parameter(description = "Whether to include current time span or not. True by default.") includeCurrent: Boolean?): ResponseEntity<List<TimeSpanDto>>

    @Operation(summary = "Get longest time span.")
    @ApiResponses(
        ApiResponse(
            description = "The longest time span.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = TimeSpanDto::class)
            )]
        ), ApiResponse(
            description = "If there are no available time spans.", responseCode = "422", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @GetMapping(ActivityMappingConstants.GET_LONGEST_TIME_SPAN)
    fun getLongestTimeSpan(
        @RequestParam(required = false) @Parameter(description = "The type of the longest time span to search for.") type: TimeSpanType?,
        @RequestParam(required = false) @Parameter(description = "Whether to process current time span or not. True by default.") processCurrent: Boolean?
    ): ResponseEntity<TimeSpanDto>

    @Operation(summary = "Get current time span.")
    @ApiResponses(
        ApiResponse(
            description = "Current time span.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = TimeSpanDto::class)
            )]
        ), ApiResponse(
            description = "If there are no available time spans.", responseCode = "422", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @GetMapping(ActivityMappingConstants.GET_CURRENT_TIMESPAN)
    fun getCurrentTimeSpan(): ResponseEntity<TimeSpanDto>

    @Operation(
        summary = "Get last registered time span.",
        description = "This time span differs from the current one in that it is calculated without the current inactivity / activity, the endBeat value is always exists."
    )
    @ApiResponses(
        ApiResponse(
            description = "Current time span.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = TimeSpanDto::class)
            )]
        ), ApiResponse(
            description = "If there are no available time spans.", responseCode = "422", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @GetMapping(ActivityMappingConstants.GET_LAST_TIMESPAN)
    fun getLastRegisteredTimeSpan(): ResponseEntity<TimeSpanDto>
}