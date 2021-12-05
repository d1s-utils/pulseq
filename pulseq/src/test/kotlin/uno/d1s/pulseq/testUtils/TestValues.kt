package uno.d1s.pulseq.testUtils

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.domain.activity.TimeSpanType
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.DeviceDto
import uno.d1s.pulseq.event.AbstractNotifiableEvent
import uno.d1s.pulseq.event.impl.inactivity.InactivityRelevanceLevel
import uno.d1s.pulseq.statistic.Statistic
import java.time.Duration
import java.time.Instant


// constants
internal const val VALID_STUB = "valid_stub"
internal const val INVALID_STUB = "invalid_stub"

// devices
internal val testDevice = Device(VALID_STUB).apply {
    id = VALID_STUB
}
internal val testDevices = listOf(testDevice)
internal val testDeviceDto = DeviceDto(VALID_STUB).apply {
    id = VALID_STUB
    beats = listOf(VALID_STUB)
}
internal val testDevicesDto = listOf(testDeviceDto)


// beats
internal val testBeat = Beat(testDevice, Duration.ZERO, Instant.EPOCH).apply {
    id = VALID_STUB

    device.beats = listOf(this)
}
internal val testBeats = listOf(testBeat)
internal val testBeatDto = BeatDto(VALID_STUB, Instant.EPOCH, Duration.ZERO).apply {
    id = VALID_STUB
}
internal val testBeatsDto = listOf(testBeatDto)

// statistics
internal val testStatistic = object : Statistic {
    override val identify = VALID_STUB
    override val title = VALID_STUB
    override val description = VALID_STUB
    override val shortDescription = VALID_STUB
}
internal val testStatistics = listOf(testStatistic)


// events
internal val testEvent = object : AbstractNotifiableEvent(VALID_STUB) {
    override val notificationMessage = VALID_STUB
}

// time spans
internal val testTimeSpan =
    TimeSpan(Duration.ZERO, TimeSpanType.ACTIVITY, InactivityRelevanceLevel.COMMON, testBeat, testBeat)

// collections
internal val testCollection = listOf(1, 2, 3)