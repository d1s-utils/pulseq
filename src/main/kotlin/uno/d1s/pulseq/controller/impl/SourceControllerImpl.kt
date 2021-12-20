/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import uno.d1s.pulseq.configuration.property.PaginationConfigurationProperties
import uno.d1s.pulseq.controller.SourceController
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.source.SourceDto
import uno.d1s.pulseq.dto.source.SourcePatchDto
import uno.d1s.pulseq.service.SourceService
import uno.d1s.pulseq.strategy.source.SourceFindingStrategyType
import uno.d1s.pulseq.strategy.source.byStrategyType
import uno.d1s.pulseq.util.page
import javax.validation.Valid

@RestController
class SourceControllerImpl : SourceController {

    @Autowired
    private lateinit var sourceService: SourceService

    @Autowired
    private lateinit var sourceDtoConverter: DtoConverter<Source, SourceDto>

    @Autowired
    private lateinit var sourcePatchDtoConverter: DtoConverter<Source, SourcePatchDto>

    @Autowired
    private lateinit var beatDtoConverter: DtoConverter<Beat, BeatDto>

    @Autowired
    private lateinit var paginationConfigurationProperties: PaginationConfigurationProperties

    @Transactional(readOnly = true)
    override fun getAllSources(
        page: Int?,
        pageSize: Int?
    ): ResponseEntity<Page<SourceDto>> = ResponseEntity.ok(
        sourceDtoConverter.convertToDtoList(
            sourceService.findAllRegisteredSources()
        ).page(page ?: 0, pageSize ?: paginationConfigurationProperties.defaultPageSize),
    )

    @Transactional(readOnly = true)
    override fun getSourceByIdentify(
        identify: String, findingStrategy: SourceFindingStrategyType?
    ): ResponseEntity<SourceDto> = ResponseEntity.ok(
        sourceDtoConverter.convertToDto(
            sourceService.findSource(
                findingStrategy.thisStrategyOrAll(identify)
            )
        )
    )

    @Transactional
    override fun registerNewSource(@Valid source: SourcePatchDto): ResponseEntity<SourceDto> {
        val createdSource = sourceDtoConverter.convertToDto(
            sourceService.registerNewSource(source.sourceName)
        )

        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequestUri().buildAndExpand(createdSource.id!!).toUri()
        ).body(createdSource)
    }

    @Transactional
    override fun getSourceBeats(
        identify: String,
        findingStrategy: SourceFindingStrategyType?,
        page: Int?,
        pageSize: Int?
    ): ResponseEntity<Page<BeatDto>> = ResponseEntity.ok(
        beatDtoConverter.convertToDtoList(
            sourceService.findSourceBeats(findingStrategy.thisStrategyOrAll(identify))
        ).page(page ?: 0, pageSize ?: paginationConfigurationProperties.defaultPageSize)
    )

    @Transactional
    override fun patchSource(
        identify: String, findingStrategy: SourceFindingStrategyType?, patch: SourcePatchDto
    ): ResponseEntity<SourceDto> = ResponseEntity.accepted().body(
        sourceDtoConverter.convertToDto(
            sourceService.updateSource(
                findingStrategy.thisStrategyOrAll(identify), sourcePatchDtoConverter.convertToDomain(patch)
            )
        )
    )

    @Transactional
    override fun deleteSource(identify: String, findingStrategy: SourceFindingStrategyType?): ResponseEntity<Any> {
        sourceService.deleteSource(findingStrategy.thisStrategyOrAll(identify))
        return ResponseEntity.noContent().build()
    }

    private fun SourceFindingStrategyType?.thisStrategyOrAll(identify: String) =
        byStrategyType(identify, this ?: SourceFindingStrategyType.BY_ALL)
}