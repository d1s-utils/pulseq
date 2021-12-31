/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.converter.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.converter.DtoConverterFacade
import uno.d1s.pulseq.domain.activity.impl.BeatInterval
import uno.d1s.pulseq.dto.beat.BeatIntervalDto
import uno.d1s.pulseq.service.BeatService

@Component
class BeatIntervalDtoConverter : DtoConverterFacade<BeatInterval, BeatIntervalDto>() {

    @Autowired
    private lateinit var beatService: BeatService

    override fun convertToDto(domain: BeatInterval): BeatIntervalDto = BeatIntervalDto(
        domain.duration,
        domain.type,
        domain.start.id ?: throw IllegalArgumentException("Start beat id could not be null."),
        domain.end?.id
    )

    override fun convertToDomain(dto: BeatIntervalDto): BeatInterval = BeatInterval(
        dto.duration, dto.type, beatService.findById(dto.startBeat), if (dto.endBeat != null) {
            beatService.findById(dto.endBeat)
        } else {
            null
        }
    )
}