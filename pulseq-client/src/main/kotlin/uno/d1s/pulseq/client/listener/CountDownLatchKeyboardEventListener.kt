package uno.d1s.pulseq.client.listener

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import uno.d1s.pulseq.client.constant.KeyboardListeningModeConstants
import uno.d1s.pulseq.client.event.KeyboardActivityDetectedEvent
import uno.d1s.pulseq.client.service.BeatSenderService

@Component
@ConditionalOnProperty(
    prefix = "pulseq.client",
    name = ["keyboard-listening-mode"],
    havingValue = KeyboardListeningModeConstants.COUNT_DOWN_LATCH
)
class CountDownLatchKeyboardEventListener {

    @Autowired
    private lateinit var beatSenderService: BeatSenderService

    private val logger = LogManager.getLogger()

    // TODO: 11/3/21 make this configurable
    private val total = 5
    private var count = reset()

    @EventListener
    fun handleKeyboardEvent(event: KeyboardActivityDetectedEvent) {
        if (count == 0) {
            reset()
        }

        count -= 1
        logger.debug("Key typed: ${event.key}; Count: ${total - count}/$total")

        if (count == 0) {
            beatSenderService.sendBeat()
        }
    }

    private fun reset(): Int = total.also {
        count = it
    }
}