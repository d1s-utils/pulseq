/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uno.d1s.pulseq.configuration.property.PaginationConfigurationProperties
import uno.d1s.pulseq.controller.ActivityController
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.domain.activity.TimeSpanType
import uno.d1s.pulseq.dto.TimeSpanDto
import uno.d1s.pulseq.service.ActivityService
import uno.d1s.pulseq.util.page

@RestController
class ActivityControllerImpl : ActivityController {

    @Autowired
    private lateinit var activityService: ActivityService

    @Autowired
    private lateinit var timeSpanDtoConverter: DtoConverter<TimeSpan, TimeSpanDto>

    @Autowired
    private lateinit var paginationConfigurationProperties: PaginationConfigurationProperties

    override fun getAllTimeSpans(
        includeCurrent: Boolean?,
        page: Int?,
        pageSize: Int?
    ): ResponseEntity<Page<TimeSpanDto>> = ResponseEntity.ok(
        timeSpanDtoConverter.convertToDtoList(
            activityService.getAllTimeSpans(includeCurrent ?: true)
        ).page(page ?: 0, pageSize ?: paginationConfigurationProperties.defaultPageSize)
    )

    override fun getLongestTimeSpan(
        @RequestParam(required = false) type: TimeSpanType?, @RequestParam(required = false) processCurrent: Boolean?
    ): ResponseEntity<TimeSpanDto> = ResponseEntity.ok(
        timeSpanDtoConverter.convertToDto(
            activityService.getLongestTimeSpan(type, processCurrent ?: true)
        )
    )

    override fun getCurrentTimeSpan(): ResponseEntity<TimeSpanDto> = ResponseEntity.ok(
        timeSpanDtoConverter.convertToDto(
            activityService.getCurrentTimeSpan()
        )
    )

    override fun getLastRegisteredTimeSpan(): ResponseEntity<TimeSpanDto> = ResponseEntity.ok(
        timeSpanDtoConverter.convertToDto(
            activityService.getLastRegisteredTimeSpan()
        )
    )
}