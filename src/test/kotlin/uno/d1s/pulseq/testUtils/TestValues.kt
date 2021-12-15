package uno.d1s.pulseq.testUtils

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.domain.InactivityRelevanceLevel
import uno.d1s.pulseq.domain.activity.TimeSpan
import uno.d1s.pulseq.domain.activity.TimeSpanType
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.TimeSpanDto
import uno.d1s.pulseq.dto.device.DeviceDto
import uno.d1s.pulseq.dto.device.DevicePatchDto
import uno.d1s.pulseq.metric.Metric
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
internal val testDeviceUpdate = Device("new-name").apply {
    id = testDevice.id
    beats = testDevice.beats
}
internal val testDeviceUpdates = listOf(testDeviceUpdate)
internal val testDeviceUpdateDto = DeviceDto(testDeviceUpdate.name).apply {
    id = testDeviceUpdate.id
    beats = listOf(VALID_STUB)
}
internal val testDevicePatchDto = DevicePatchDto("new-name")
internal val testDevicePatchesDto = listOf(testDevicePatchDto)


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

// metrics
internal val testMetric = object : Metric {
    override val identify = VALID_STUB
    override val title = VALID_STUB
    override val description = VALID_STUB
    override val shortDescription = VALID_STUB
}
internal val testMetrics = listOf(testMetric)

// time spans
internal val testTimeSpan =
    TimeSpan(Duration.ZERO, TimeSpanType.ACTIVITY, InactivityRelevanceLevel.COMMON, testBeat, testBeat)
internal val testTimeSpans = listOf(testTimeSpan)
internal val testTimeSpanDto = TimeSpanDto(
    testTimeSpan.duration,
    testTimeSpan.type,
    testTimeSpan.inactivityLevel,
    testTimeSpan.startBeat.id!!,
    testTimeSpan.endBeat!!.id!!
)
internal val testTimeSpansDto = listOf(testTimeSpanDto)

// collections
internal val testCollection = listOf(1, 2, 3)