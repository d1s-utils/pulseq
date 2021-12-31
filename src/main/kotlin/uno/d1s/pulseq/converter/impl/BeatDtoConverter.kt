/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.converter.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.converter.DtoConverterFacade
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.service.SourceService
import uno.d1s.pulseq.strategy.source.byId

@Component
class BeatDtoConverter : DtoConverterFacade<Beat, BeatDto>() {

    @Autowired
    private lateinit var sourceService: SourceService

    override fun convertToDto(domain: Beat): BeatDto = BeatDto(
        domain.source.id ?: throw IllegalArgumentException("Source id could not be null."),
        domain.beatTime,
        domain.inactivityBeforeBeat
    ).apply {
        domain.id?.let {
            id = it
        }
    }

    override fun convertToDomain(dto: BeatDto): Beat = Beat(
        sourceService.findSource(byId(dto.source)), dto.inactivityBeforeBeat, dto.beatTime
    ).apply {
        dto.id?.let {
            id = it
        }
    }
}