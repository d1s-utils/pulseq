package uno.d1s.pulseq.converter

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import uno.d1s.pulseq.*
import uno.d1s.pulseq.converter.impl.BeatDtoConverter
import uno.d1s.pulseq.service.DeviceService

@SpringBootTest
internal class BeatDtoConverterTest {

    @Autowired
    private lateinit var beatDtoConverter: BeatDtoConverter

    @MockkBean
    private lateinit var deviceService: DeviceService

    @BeforeEach
    fun setup() {
        every {
            deviceService.findDeviceById(VALID_STUB)
        } returns testDevice
    }

    @Test
    fun `should return valid dto on conversion to dto`() {
        Assertions.assertEquals(testBeatDto, beatDtoConverter.convertToDto(testBeat))
    }

    @Test
    fun `should throw an exception on conversion to dto with null device id`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            beatDtoConverter.convertToDto(testBeat.apply {
                device.id = null
            })
        }
    }

    @Test
    fun `should return valid list on conversion to dto list`() {
        Assertions.assertEquals(testBeatsDto, beatDtoConverter.convertToDtoList(testBeats))
    }

    @Test
    fun `should return valid list on conversion to domain list`() {
        Assertions.assertEquals(testBeats, beatDtoConverter.convertToDomainList(testBeatsDto))
    }
}