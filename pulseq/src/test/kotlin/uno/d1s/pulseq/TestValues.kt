package uno.d1s.pulseq

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.dto.BeatDto
import uno.d1s.pulseq.dto.DeviceDto
import java.time.Instant

const val VALID_ID = "valid_id"
const val INVALID_ID = "invalid_id"

val testDevice = Device(VALID_ID)
val testDevices = listOf<Device>()
val testDeviceDto = DeviceDto(VALID_ID)
val testDevicesDto = listOf<DeviceDto>()

val testBeat = Beat(testDevice, null)
val testBeats = listOf<Beat>()
val testBeatDto = BeatDto(VALID_ID, Instant.now(), null)
val testBeatsDto = listOf<BeatDto>()
