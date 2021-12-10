package uno.d1s.pulseq.converter

import com.ninjasquad.springmockk.SpykBean
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.converter.impl.DevicePatchDtoConverter
import uno.d1s.pulseq.testUtils.testDevicePatchDto
import uno.d1s.pulseq.testUtils.testDeviceUpdate

@SpringBootTest
@ContextConfiguration(classes = [DevicePatchDtoConverter::class])
internal class DevicePatchDtoConverterTest {

    @SpykBean
    private lateinit var devicePatchDtoConverter: DevicePatchDtoConverter

    @Test
    fun `should return valid dto`() {
        Assertions.assertEquals(testDevicePatchDto, devicePatchDtoConverter.convertToDto(testDeviceUpdate))
    }

    @Test
    fun `should return valid domain`() {
        Assertions.assertEquals(testDeviceUpdate, devicePatchDtoConverter.convertToDomain(testDevicePatchDto))
    }
}