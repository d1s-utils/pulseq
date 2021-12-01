package uno.d1s.pulseq.testUtils

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.domain.activity.TimeSpanType
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.DeviceDto
import uno.d1s.pulseq.event.AbstractNotifiableEvent
import uno.d1s.pulseq.statistic.Statistic
import java.time.Duration
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
    get() = Beat(testDevice, Duration.ZERO, Instant.EPOCH).apply {
        id = VALID_STUB
    }
internal val testBeats get() = listOf<Beat>()
internal val testBeatDto
    get() = BeatDto(VALID_STUB, Instant.EPOCH, Duration.ZERO).apply {
        id = VALID_STUB
    }
internal val testBeatsDto get() = listOf<BeatDto>()

internal val testStatistic
    get() = object : Statistic {
        override val identify = VALID_STUB
        override val title = VALID_STUB
        override val description = VALID_STUB
        override val shortDescription = VALID_STUB
    }
internal val testStatistics get() = listOf<Statistic>()

internal val testEvent
    get() = object : AbstractNotifiableEvent(VALID_STUB) {
        override val notificationMessage = VALID_STUB
    }

internal val testTimeSpan
    get() = TimeSpan(Duration.ZERO, TimeSpanType.ACTIVITY, testBeat, testBeat)

internal val testCollection = listOf(1, 2, 3)