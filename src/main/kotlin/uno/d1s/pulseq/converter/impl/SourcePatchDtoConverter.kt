/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.converter.impl

import org.springframework.stereotype.Component
import uno.d1s.pulseq.converter.DtoConverterFacade
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.dto.source.SourcePatchDto

@Component
class SourcePatchDtoConverter : DtoConverterFacade<Source, SourcePatchDto>() {

    override fun convertToDto(domain: Source): SourcePatchDto = SourcePatchDto(
        domain.name
    )

    override fun convertToDomain(dto: SourcePatchDto): Source = Source(
        dto.sourceName
    )
}