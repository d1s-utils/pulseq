package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.strategy.device.DeviceFindingStrategy

interface BeatService {

    fun findBeatById(id: String): Beat

    fun registerNewBeatWithDeviceIdentify(identify: String): Beat

    fun findAllByDevice(
        strategy: DeviceFindingStrategy
    ): List<Beat>

    fun findAllBeats(): List<Beat>

    fun totalBeats(): Int

    fun totalBeatsByDevices(): Map<String, Int>

    fun findLastBeat(): Beat

    fun findFirstBeat(): Beat
}