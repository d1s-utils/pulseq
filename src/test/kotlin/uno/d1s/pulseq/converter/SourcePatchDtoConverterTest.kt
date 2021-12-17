package uno.d1s.pulseq.converter

import com.ninjasquad.springmockk.SpykBean
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.converter.impl.SourcePatchDtoConverter
import uno.d1s.pulseq.testUtils.testSourcePatchDto
import uno.d1s.pulseq.testUtils.testSourceUpdate

@SpringBootTest
@ContextConfiguration(classes = [SourcePatchDtoConverter::class])
internal class SourcePatchDtoConverterTest {

    @SpykBean
    private lateinit var sourcePatchDtoConverter: SourcePatchDtoConverter

    @Test
    fun `should return valid dto`() {
        Assertions.assertEquals(testSourcePatchDto, sourcePatchDtoConverter.convertToDto(testSourceUpdate))
    }

    @Test
    fun `should return valid domain`() {
        Assertions.assertEquals(testSourceUpdate, sourcePatchDtoConverter.convertToDomain(testSourcePatchDto))
    }
}