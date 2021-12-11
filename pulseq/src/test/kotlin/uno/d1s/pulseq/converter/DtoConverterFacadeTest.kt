package uno.d1s.pulseq.converter

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.converter.impl.DevicePatchDtoConverter
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.device.DevicePatchDto
import uno.d1s.pulseq.testUtils.testDevicePatchesDto
import uno.d1s.pulseq.testUtils.testDeviceUpdates

@SpringBootTest
@ContextConfiguration(classes = [DevicePatchDtoConverter::class])
class DtoConverterFacadeTest {

    // it doesn't really matter which dto converter do I use, need to test the adapter logic
    // devicePatchDtoConverter is mostly independent.
    @Autowired
    private lateinit var devicePatchDtoConverter: DtoConverter<Device, DevicePatchDto>

    @Test
    fun `should return valid list on conversion to dto list`() {
        Assertions.assertEquals(testDevicePatchesDto, devicePatchDtoConverter.convertToDtoList(testDeviceUpdates))
    }

    @Test
    fun `should return valid list on conversion to domain list`() {
        Assertions.assertEquals(testDeviceUpdates, devicePatchDtoConverter.convertToDomainList(testDevicePatchesDto))
    }
}