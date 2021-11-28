package uno.d1s.pulseq.client.listener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import uno.d1s.pulseq.client.configuration.properties.KeyboardListeningConfigurationProperties
import uno.d1s.pulseq.client.event.KeyboardActivityDetectedEvent
import uno.d1s.pulseq.client.service.BeatSenderService

@Component
@ConditionalOnProperty("pulseq.client.keyboard-listening-mode.timer.enabled")
class TimerKeyboardEventListener : ApplicationListener<KeyboardActivityDetectedEvent> {

    @Autowired
    private lateinit var beatSenderService: BeatSenderService

    @Autowired
    private lateinit var keyboardListeningConfigurationProperties: KeyboardListeningConfigurationProperties

    private var keyTyped = false

    private var jobStarted = false

    private val delay
        get() = keyboardListeningConfigurationProperties.timer.delay.toMillis()

    override fun onApplicationEvent(event: KeyboardActivityDetectedEvent) {
        if (!jobStarted) {
            startJob()
            jobStarted = true
        }

        keyTyped = true
    }

    private fun startJob() {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                if (keyTyped) {
                    beatSenderService.sendBeat()
                    keyTyped = false
                }

                delay(delay)
            }
        }
    }
}
