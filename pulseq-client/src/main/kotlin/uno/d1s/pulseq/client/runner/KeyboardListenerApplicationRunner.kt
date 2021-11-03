package uno.d1s.pulseq.client.runner

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import uno.d1s.pulseq.client.event.KeyboardActivityDetectedEvent

@Component
class KeyboardListenerApplicationRunner : ApplicationRunner {

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    override fun run(args: ApplicationArguments) {
        try {
            GlobalScreen.registerNativeHook()
        } catch (ex: NativeHookException) {
            println(ex.message)
        }

        GlobalScreen.addNativeKeyListener(
            object : NativeKeyListener {
                override fun nativeKeyTyped(nativeEvent: NativeKeyEvent) {
                    applicationEventPublisher.publishEvent(
                        KeyboardActivityDetectedEvent(this, nativeEvent.keyChar)
                    )
                }
            }
        )
    }
}