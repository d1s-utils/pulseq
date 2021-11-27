package uno.d1s.pulseq

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.DeviceDto
import uno.d1s.pulseq.statistic.Statistic
import java.time.Instant

internal const val VALID_STUB = "valid_stub"
internal const val INVALID_STUB = "invalid_stub"

internal val testDevice
    get() = Device(VALID_STUB).apply {
        id = VALID_STUB
        beats = testBeats
    }
internal val testDevices get() = listOf<Device>()
internal val testDeviceDto
    get() = DeviceDto(VALID_STUB).apply {
        id = VALID_STUB
        beats = listOf()
    }
internal val testDevicesDto get() = listOf<DeviceDto>()

internal val testBeat
    get() = Beat(testDevice, null, Instant.EPOCH).apply {
        id = VALID_STUB
    }
internal val testBeats get() = listOf<Beat>()
internal val testBeatDto get() = BeatDto(VALID_STUB, Instant.EPOCH, null)
internal val testBeatsDto get() = listOf<BeatDto>()

internal val testStatistic
    get() = object : Statistic {
        override val identify = VALID_STUB
        override val title = VALID_STUB
        override val description = VALID_STUB
        override val shortDescription = VALID_STUB
    }
internal val testStatistics get() = listOf<Statistic>()