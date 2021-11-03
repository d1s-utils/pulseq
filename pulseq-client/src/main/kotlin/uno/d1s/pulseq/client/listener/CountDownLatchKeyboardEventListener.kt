package uno.d1s.pulseq.client.listener

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import uno.d1s.pulseq.client.configuration.properties.KeyboardListeningModeConfigurationProperties
import uno.d1s.pulseq.client.event.KeyboardActivityDetectedEvent
import uno.d1s.pulseq.client.service.BeatSenderService

@Component
@ConditionalOnProperty(
    prefix = "pulseq.client.keyboard-listening-mode.count-down-latch",
    name = ["enabled"],
    havingValue = "true"
)
class CountDownLatchKeyboardEventListener {

    @Autowired
    private lateinit var beatSenderService: BeatSenderService

    @Autowired
    private lateinit var keyboardListeningModeConfigurationProperties: KeyboardListeningModeConfigurationProperties

    private val logger = LogManager.getLogger()

    private var count = reset()

    private val total
        get() = keyboardListeningModeConfigurationProperties.countDownLatch.countTrigger

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