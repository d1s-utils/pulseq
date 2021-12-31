/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
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
import uno.d1s.pulseq.domain.activity.impl.BeatInterval
import uno.d1s.pulseq.domain.activity.IntervalType
import uno.d1s.pulseq.dto.beat.BeatIntervalDto
import uno.d1s.pulseq.service.IntervalService
import uno.d1s.pulseq.util.page

@RestController
class ActivityControllerImpl : ActivityController {

    @Autowired
    private lateinit var intervalService: IntervalService

    @Autowired
    private lateinit var beatBeatIntervalDtoConverter: DtoConverter<BeatInterval, BeatIntervalDto>

    @Autowired
    private lateinit var paginationConfigurationProperties: PaginationConfigurationProperties

    override fun getAllDurations(
        includeCurrent: Boolean?,
        page: Int?,
        pageSize: Int?
    ): ResponseEntity<Page<BeatIntervalDto>> = ResponseEntity.ok(
        beatBeatIntervalDtoConverter.convertToDtoList(
            intervalService.findAllIntervals(includeCurrent ?: true)
        ).page(page ?: 0, pageSize ?: paginationConfigurationProperties.defaultPageSize)
    )

    override fun getLongestDuration(
        @RequestParam(required = false) type: IntervalType?, @RequestParam(required = false) processCurrent: Boolean?
    ): ResponseEntity<BeatIntervalDto> = ResponseEntity.ok(
        beatBeatIntervalDtoConverter.convertToDto(
            intervalService.findLongestInterval(type, processCurrent ?: true)
        )
    )

    override fun getCurrentDuration(): ResponseEntity<BeatIntervalDto> = ResponseEntity.ok(
        beatBeatIntervalDtoConverter.convertToDto(
            intervalService.findCurrentInterval()
        )
    )

    override fun getLastRegisteredDuration(): ResponseEntity<BeatIntervalDto> = ResponseEntity.ok(
        beatBeatIntervalDtoConverter.convertToDto(
            intervalService.getLastRegisteredDuration()
        )
    )
}