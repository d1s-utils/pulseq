package uno.d1s.pulseq.converter

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import uno.d1s.pulseq.*
import uno.d1s.pulseq.converter.impl.DeviceDtoConverter
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.service.BeatService

@SpringBootTest
internal class DeviceDtoConverterTest {

    @Autowired
    private lateinit var deviceDtoConverter: DeviceDtoConverter

    @MockkBean
    private lateinit var beatDtoConverter: DtoConverter<Beat, BeatDto>

    @MockkBean
    private lateinit var beatService: BeatService

    @BeforeEach
    fun setup() {
        every {
            beatDtoConverter.convertToDtoList(testBeats)
        } returns testBeatsDto

        every {
            beatService.findBeatById(VALID_STUB)
        } returns testBeat
    }

    @Test
    fun `should return valid dto on conversion to dto`() {
        Assertions.assertEquals(testDeviceDto, deviceDtoConverter.convertToDto(testDevice))

        verify {
            beatDtoConverter.convertToDtoList(testBeats)
        }
    }

    @Test
    fun `should return valid domain on conversion to domain`() {
        Assertions.assertEquals(testDevice, deviceDtoConverter.convertToDomain(testDeviceDto))
    }

    @Test
    fun `should return valid list on conversion to dto list`() {
        Assertions.assertEquals(testDevicesDto, deviceDtoConverter.convertToDtoList(testDevices))
    }

    @Test
    fun `should return valid list on conversion to domain list`() {
        Assertions.assertEquals(testDevices, deviceDtoConverter.convertToDomainList(testDevicesDto))
    }
}