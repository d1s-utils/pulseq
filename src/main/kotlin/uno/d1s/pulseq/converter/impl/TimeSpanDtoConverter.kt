/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.converter.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.converter.DtoConverterFacade
import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.dto.TimeSpanDto
import uno.d1s.pulseq.service.BeatService

@Component
class TimeSpanDtoConverter : DtoConverterFacade<TimeSpan, TimeSpanDto>() {

    @Autowired
    private lateinit var beatService: BeatService

    override fun convertToDto(domain: TimeSpan): TimeSpanDto = TimeSpanDto(
        domain.duration,
        domain.type,
        domain.startBeat.id ?: throw IllegalArgumentException("Start beat id could not be null."),
        domain.endBeat?.id
    )

    override fun convertToDomain(dto: TimeSpanDto): TimeSpan = TimeSpan(
        dto.duration, dto.type, beatService.findBeatById(dto.startBeat), if (dto.endBeat != null) {
            beatService.findBeatById(dto.endBeat)
        } else {
            null
        }
    )
}