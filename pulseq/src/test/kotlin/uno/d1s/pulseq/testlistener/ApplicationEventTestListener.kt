package uno.d1s.pulseq.testlistener

import org.springframework.context.ApplicationEvent
import org.springframework.context.event.EventListener


// the reason of this class is https://github.com/spring-projects/spring-framework/issues/18907
// spring is not allows you to mock the application event publisher yet.
class ApplicationEventTestListener {

    lateinit var lastInterceptedEvent: ApplicationEvent

    @EventListener
    fun intercept(event: ApplicationEvent) {
        lastInterceptedEvent = event
    }

    fun isLastEventIntercepted() =
        this::lastInterceptedEvent.isInitialized

    inline fun <reified T : ApplicationEvent> isLastEventWas() =
        this.isLastEventIntercepted() && lastInterceptedEvent is T
}
