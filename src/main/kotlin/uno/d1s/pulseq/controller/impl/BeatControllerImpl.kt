/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import uno.d1s.pulseq.configuration.property.PaginationConfigurationProperties
import uno.d1s.pulseq.controller.BeatController
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.exception.impl.SourceNotFoundException
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.util.page
import javax.servlet.http.HttpServletResponse


@RestController
class BeatControllerImpl : BeatController {

    @Autowired
    private lateinit var beatService: BeatService

    @Autowired
    private lateinit var beatDtoConverter: DtoConverter<Beat, BeatDto>

    @Autowired
    private lateinit var paginationConfigurationProperties: PaginationConfigurationProperties

    override fun getBeatBtId(id: String): ResponseEntity<BeatDto> = ResponseEntity.ok(
        beatDtoConverter.convertToDto(
            beatService.findBeatById(id)
        )
    )


    override fun registerNewBeatWithSourceIdentify(
        sourceParam: String?, sourceHeader: String?, response: HttpServletResponse
    ): ResponseEntity<BeatDto>? {
        val createdBeat = beatDtoConverter.convertToDto(
            beatService.registerNewBeatWithSourceIdentify(
                sourceParam ?: (sourceHeader ?: throw SourceNotFoundException("Source definition must be present."))
            )
        )

        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequestUri().buildAndExpand(createdBeat.id!!).toUri()
        ).body(createdBeat)
    }

    override fun getBeats(
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) pageSize: Int?
    ): ResponseEntity<Page<BeatDto>> = ResponseEntity.ok(
        beatDtoConverter.convertToDtoList(
            beatService.findAllBeats()
        ).page(page ?: 0, pageSize ?: paginationConfigurationProperties.defaultPageSize)
    )

    override fun getLastBeat(): ResponseEntity<BeatDto> = ResponseEntity.ok(
        beatDtoConverter.convertToDto(
            beatService.findLastBeat()
        )
    )

    override fun deleteBeat(id: String): ResponseEntity<Any> {
        beatService.deleteBeat(id)
        return ResponseEntity.noContent().build()
    }
}