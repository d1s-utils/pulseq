package uno.d1s.pulseq.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import uno.d1s.pulseq.core.constant.mapping.ActivityMappingConstants
import uno.d1s.pulseq.dto.TimeSpanDto

interface ActivityController {

    @GetMapping(ActivityMappingConstants.GET_TIMESPANS)
    fun getAllTimeSpans(@RequestParam(required = false) includeCurrent: Boolean?): ResponseEntity<List<TimeSpanDto>>

    @GetMapping(ActivityMappingConstants.GET_LONGEST_TIME_SPAN)
    fun getLongestTimeSpan(
        @RequestParam(required = false) excludeActivity: Boolean?,
        @RequestParam(required = false) processCurrent: Boolean?
    ): ResponseEntity<TimeSpanDto>

    @GetMapping(ActivityMappingConstants.GET_CURRENT_TIMESPAN)
    fun getCurrentTimeSpan(): ResponseEntity<TimeSpanDto>

    @GetMapping(ActivityMappingConstants.GET_LAST_TIMESPAN)
    fun getLastRegisteredTimeSpan(): ResponseEntity<TimeSpanDto>
}