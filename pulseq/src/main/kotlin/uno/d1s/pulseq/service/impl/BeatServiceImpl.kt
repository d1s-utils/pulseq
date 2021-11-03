package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.domain.Device
import uno.d1s.pulseq.event.DelayedBeatReceivedEvent
import uno.d1s.pulseq.exception.BeatNotFoundException
import uno.d1s.pulseq.exception.NoBeatsReceivedException
import uno.d1s.pulseq.repository.BeatRepository
import uno.d1s.pulseq.service.BeatService
import uno.d1s.pulseq.service.DeviceService
import uno.d1s.pulseq.service.InactivityStatusService
import uno.d1s.pulseq.util.findClosestInstantToCurrent
import kotlin.properties.Delegates

@Service("beatService")
class BeatServiceImpl : BeatService {

    @Autowired
    private lateinit var beatRepository: BeatRepository

    @Autowired
    private lateinit var deviceService: DeviceService

    @Autowired
    private lateinit var inactivityService: InactivityStatusService

    @Autowired
    private lateinit var eventPublisher: ApplicationEventPublisher

    @Transactional(readOnly = true)
    override fun findBeatById(id: String): Beat =
        beatRepository.findById(id).orElseThrow {
            BeatNotFoundException("Could not find any beats with provided id.")
        }

    @Transactional
    override fun registerNewBeatWithDeviceIdentify(identify: String): Beat {
        var device: Device by Delegates.notNull()

        runCatching {
            device = deviceService.findDeviceByIdentify(identify)
        }.onFailure {
            device = deviceService.registerNewDevice(identify)
        }

        Beat(
            device,
            runCatching {
                inactivityService.getCurrentInactivity()
            }.getOrElse {
                null
            }).let { unsavedBeat ->
            if (inactivityService.isRelevanceLevelNotCommon()) {
                beatRepository.save(unsavedBeat).let { savedBeat ->
                    eventPublisher.publishEvent(
                        DelayedBeatReceivedEvent(
                            this,
                            savedBeat
                        )
                    )
                    return savedBeat
                }
            } else {
                return beatRepository.save(unsavedBeat)
            }
        }
    }

    @Transactional(readOnly = true)
    override fun findAllBeatsByDeviceId(deviceId: String): List<Beat> =
        beatRepository.findAllByDeviceIdEquals(deviceId)

    @Transactional(readOnly = true)
    override fun findAllBeatsByDeviceName(deviceName: String): List<Beat> =
        beatRepository.findAllByDeviceNameEqualsIgnoreCase(deviceName)

    @Transactional(readOnly = true)
    override fun findAllBeatsByDeviceIdentify(deviceIdentify: String): List<Beat> =
        deviceService.findDeviceByIdentify(deviceIdentify).beats ?: listOf()

    @Transactional(readOnly = true)
    override fun findAllBeats(): List<Beat> =
        beatRepository.findAll()

    @Transactional(readOnly = true)
    override fun totalBeats(): Int =
        this.findAllBeats().size

    @Transactional(readOnly = true)
    override fun findLastBeat(): Beat =
        this.findAllBeats().let { all ->
            all.firstOrNull { beat ->
                all.map {
                    it.beatTime
                }.findClosestInstantToCurrent().orElseThrow {
                    NoBeatsReceivedException
                } == beat.beatTime
            } ?: throw NoBeatsReceivedException
        }
}