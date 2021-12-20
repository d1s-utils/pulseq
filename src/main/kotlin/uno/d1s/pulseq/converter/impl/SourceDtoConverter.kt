/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.converter.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.converter.DtoConverterFacade
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.source.SourceDto
import uno.d1s.pulseq.service.BeatService

@Component
class SourceDtoConverter : DtoConverterFacade<Source, SourceDto>() {

    @Autowired
    private lateinit var beatDtoConverter: DtoConverter<Beat, BeatDto>

    @Autowired
    private lateinit var beatService: BeatService

    override fun convertToDto(domain: Source): SourceDto = SourceDto(domain.name).apply {
        domain.id?.let {
            id = it

            domain.beats?.let { beatList ->
                beats = beatDtoConverter.convertToDtoList(beatList).map { beat ->
                    beat.id!!
                }
            }
        }
    }

    override fun convertToDomain(dto: SourceDto): Source = Source(dto.sourceName).apply {
        dto.id?.let { dtoId ->
            id = dtoId
            dto.beats?.let { beatList ->
                beats = beatList.map {
                    beatService.findBeatById(it)
                }
            }
        }
    }
}