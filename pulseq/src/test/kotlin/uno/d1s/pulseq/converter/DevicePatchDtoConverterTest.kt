package uno.d1s.pulseq.converter

import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.converter.impl.DevicePatchDtoConverter
import uno.d1s.pulseq.testUtils.testDevicePatchDto
import uno.d1s.pulseq.testUtils.testDevicePatchesDto
import uno.d1s.pulseq.testUtils.testDeviceUpdate
import uno.d1s.pulseq.testUtils.testDeviceUpdates

@SpringBootTest
@ContextConfiguration(classes = [DevicePatchDtoConverter::class])
class DevicePatchDtoConverterTest {

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

    @Test
    fun `should return valid list on conversion to dto list`() {
        Assertions.assertEquals(
            testDevicePatchesDto, devicePatchDtoConverter.convertToDtoList(testDeviceUpdates)
        )

        verify {
            devicePatchDtoConverter.convertToDto(any())
        }
    }

    @Test
    fun `should return valid list on conversion to domain list`() {
        Assertions.assertEquals(
            testDeviceUpdates, devicePatchDtoConverter.convertToDomainList(testDevicePatchesDto)
        )

        verify {
            devicePatchDtoConverter.convertToDomain(any())
        }
    }
}