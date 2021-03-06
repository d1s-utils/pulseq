/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.converter

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.converter.impl.TimeSpanDtoConverter
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.testUtils.VALID_STUB
import uno.d1s.pulseq.testUtils.testBeat
import uno.d1s.pulseq.testUtils.testTimeSpan
import uno.d1s.pulseq.testUtils.testTimeSpanDto

@SpringBootTest
@ContextConfiguration(classes = [TimeSpanDtoConverter::class])
class TimeSpanDtoConverterTest {

    @Autowired
    private lateinit var timeSpanDtoConverter: TimeSpanDtoConverter

    @MockkBean
    private lateinit var beatService: BeatService

    @BeforeEach
    fun setup() {
        every {
            beatService.findBeatById(VALID_STUB)
        } returns testBeat
    }

    @Test
    fun `should return valid dto`() {
        Assertions.assertEquals(testTimeSpanDto, timeSpanDtoConverter.convertToDto(testTimeSpan))
    }

    @Test
    fun `should return valid domain`() {
        Assertions.assertEquals(testTimeSpan, timeSpanDtoConverter.convertToDomain(testTimeSpanDto))
    }
}