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
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.strategy.device.byId
import uno.d1s.pulseq.testUtils.VALID_STUB
import uno.d1s.pulseq.testUtils.testBeat
import uno.d1s.pulseq.testUtils.testBeatDto
import uno.d1s.pulseq.testUtils.testDevice

@SpringBootTest
@ContextConfiguration(classes = [BeatDtoConverter::class])
internal class BeatDtoConverterTest {

    @SpykBean
    private lateinit var beatDtoConverter: BeatDtoConverter

    @MockkBean
    private lateinit var deviceService: DeviceService

    @BeforeEach
    fun setup() {
        every {
            deviceService.findDevice(byId(VALID_STUB))
        } returns testDevice
    }

    @Test
    fun `should return valid dto on conversion to dto`() {
        Assertions.assertEquals(testBeatDto, beatDtoConverter.convertToDto(testBeat))
    }

    @Test
    fun `should throw an exception on conversion to dto with null device id`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            val nullDeviceIdBeat = Beat(
                Device(VALID_STUB),
                testBeat.inactivityBeforeBeat
            )

            beatDtoConverter.convertToDto(nullDeviceIdBeat)
        }
    }
}