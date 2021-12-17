package uno.d1s.pulseq.converter

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.converter.impl.SourcePatchDtoConverter
import uno.d1s.pulseq.domain.Source
import uno.d1s.pulseq.dto.source.SourcePatchDto
import uno.d1s.pulseq.testUtils.testSourcePatchesDto
import uno.d1s.pulseq.testUtils.testSourceUpdates

@SpringBootTest
@ContextConfiguration(classes = [SourcePatchDtoConverter::class])
class DtoConverterFacadeTest {

    // it doesn't really matter which dto converter do I use, need to test the adapter logic
    // sourcePatchDtoConverter is mostly independent.
    @Autowired
    private lateinit var sourcePatchDtoConverter: DtoConverter<Source, SourcePatchDto>

    @Test
    fun `should return valid list on conversion to dto list`() {
        Assertions.assertEquals(testSourcePatchesDto, sourcePatchDtoConverter.convertToDtoList(testSourceUpdates))
    }

    @Test
    fun `should return valid list on conversion to domain list`() {
        Assertions.assertEquals(testSourceUpdates, sourcePatchDtoConverter.convertToDomainList(testSourcePatchesDto))
    }
}