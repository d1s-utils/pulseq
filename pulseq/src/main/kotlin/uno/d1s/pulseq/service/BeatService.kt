package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.Beat

interface BeatService {

    fun findBeatById(id: String): Beat

    fun registerNewBeatWithDeviceIdentify(identify: String): Beat

    fun findAllBeatsByDeviceId(deviceId: String): List<Beat>

    fun findAllBeatsByDeviceName(deviceName: String): List<Beat>

    fun findAllBeatsByDeviceIdentify(deviceIdentify: String): List<Beat>

    fun findAllBeats(): List<Beat>

    fun totalBeats(): Int

    fun findLastBeat(): Beat
}