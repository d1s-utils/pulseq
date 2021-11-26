package uno.d1s.pulseq

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.DeviceDto
import uno.d1s.pulseq.statistic.Statistic
import java.time.Instant

const val VALID_STUB = "valid_stub"
const val INVALID_STUB = "invalid_stub"

val testDevice = Device(VALID_STUB)
val testDevices = listOf<Device>()
val testDeviceDto = DeviceDto(VALID_STUB)
val testDevicesDto = listOf<DeviceDto>()

val testBeat = Beat(testDevice, null)
val testBeats = listOf<Beat>()
val testBeatDto = BeatDto(VALID_STUB, Instant.now(), null)
val testBeatsDto = listOf<BeatDto>()

val testStatistic = object : Statistic {
    override val identify = VALID_STUB
    override val title = VALID_STUB
    override val description = VALID_STUB
    override val shortDescription = VALID_STUB
}
val testStatistics = listOf<Statistic>()