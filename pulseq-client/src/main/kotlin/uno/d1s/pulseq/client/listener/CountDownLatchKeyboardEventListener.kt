package uno.d1s.pulseq.client.listener

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import uno.d1s.pulseq.client.configuration.properties.KeyboardListeningModeConfigurationProperties
import uno.d1s.pulseq.client.event.KeyboardActivityDetectedEvent
import uno.d1s.pulseq.client.service.BeatSenderService
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque

@Component
@ConditionalOnProperty(
    prefix = "pulseq.client.keyboard-listening-mode.count-down-latch",
    name = ["enabled"],
    havingValue = "true"
)
class CountDownLatchKeyboardEventListener {

    @Autowired
    private lateinit var beatSenderService: BeatSenderService //fffff

    @Autowired
    private lateinit var keyboardListeningModeConfigurationProperties: KeyboardListeningModeConfigurationProperties

    private val logger = LogManager.getLogger()

    private val total
        get() = keyboardListeningModeConfigurationProperties.countDownLatch.countTrigger

    private var queue: Queue<Int> = ConcurrentLinkedDeque()

    @EventListener
    fun handleKeyboardEvent(event: KeyboardActivityDetectedEvent) {
        if (queue.isEmpty()) {
            queue.addAll(1..total)
        }

        queue.remove()
        logger.debug("Key typed: ${event.key}; Count: ${queue.peek() ?: 0}/$total")

        queue.peek() ?: run {
            beatSenderService.sendBeat()
        }
    }
}