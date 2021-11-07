package uno.d1s.pulseq.client.listener

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import uno.d1s.pulseq.client.configuration.properties.KeyboardListeningConfigurationProperties
import uno.d1s.pulseq.client.event.KeyboardActivityDetectedEvent
import uno.d1s.pulseq.client.service.BeatSenderService
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque

@Component
@ConditionalOnProperty(
    prefix = "pulseq.client.keyboard-listening-mode.count-down-latch",
    name = ["enabled"]
)
class CountDownLatchKeyboardEventListener : ApplicationListener<KeyboardActivityDetectedEvent> {

    @Autowired
    private lateinit var beatSenderService: BeatSenderService

    @Autowired
    private lateinit var keyboardListeningConfigurationProperties: KeyboardListeningConfigurationProperties

    private val logger = LogManager.getLogger()

    private val total
        get() = keyboardListeningConfigurationProperties.countDownLatch.countTrigger

    private var queue: Queue<Int> = ConcurrentLinkedDeque()

    override fun onApplicationEvent(event: KeyboardActivityDetectedEvent) {
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