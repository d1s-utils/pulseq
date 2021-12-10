package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import uno.d1s.pulseq.controller.ActivityController
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.dto.TimeSpanDto
import uno.d1s.pulseq.service.ActivityService

@RestController
class ActivityControllerImpl : ActivityController {

    @Autowired
    private lateinit var activityService: ActivityService

    @Autowired
    private lateinit var timeSpanDtoConverter: DtoConverter<TimeSpan, TimeSpanDto>

    override fun getAllTimeSpans(includeCurrent: Boolean?): ResponseEntity<List<TimeSpanDto>> = ResponseEntity.ok(
        timeSpanDtoConverter.convertToDtoList(
            activityService.getAllTimeSpans(includeCurrent ?: true)
        )
    )

    override fun getLongestTimeSpan(
        excludeActivity: Boolean?, processCurrent: Boolean?
    ): ResponseEntity<TimeSpanDto> = ResponseEntity.ok(
        timeSpanDtoConverter.convertToDto(
            activityService.getLongestTimeSpan(excludeActivity ?: true, processCurrent ?: true)
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