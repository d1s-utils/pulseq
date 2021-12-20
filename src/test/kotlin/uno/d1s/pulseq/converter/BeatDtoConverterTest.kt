/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.converter

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.converter.impl.BeatDtoConverter
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.service.SourceService
import uno.d1s.pulseq.strategy.source.byId
import uno.d1s.pulseq.testUtils.VALID_STUB
import uno.d1s.pulseq.testUtils.testBeat
import uno.d1s.pulseq.testUtils.testBeatDto
import uno.d1s.pulseq.testUtils.testSource

@SpringBootTest
@ContextConfiguration(classes = [BeatDtoConverter::class])
internal class BeatDtoConverterTest {

    @SpykBean
    private lateinit var beatDtoConverter: BeatDtoConverter

    @MockkBean
    private lateinit var sourceService: SourceService

    @BeforeEach
    fun setup() {
        every {
            sourceService.findSource(byId(VALID_STUB))
        } returns testSource
    }

    @Test
    fun `should return valid dto on conversion to dto`() {
        Assertions.assertEquals(testBeatDto, beatDtoConverter.convertToDto(testBeat))
    }

    @Test
    fun `should throw an exception on conversion to dto with null source id`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            val nullSourceIdBeat = Beat(
                Source(VALID_STUB),
                testBeat.inactivityBeforeBeat
            )

            beatDtoConverter.convertToDto(nullSourceIdBeat)
        }
    }
}