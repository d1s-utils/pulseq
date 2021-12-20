/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.testlistener

import org.springframework.context.ApplicationEvent
import org.springframework.context.event.EventListener


// the reason of this class is https://github.com/spring-projects/spring-framework/issues/18907
// spring is not allows you to mock the application event publisher yet.
internal class ApplicationEventTestListener {

    var lastInterceptedEvent: ApplicationEvent? = null

    @EventListener
    fun intercept(event: ApplicationEvent) {
        lastInterceptedEvent = event
    }

    inline fun <reified T : ApplicationEvent> isLastEventWas() =
        (lastInterceptedEvent != null && lastInterceptedEvent is T).also {
            lastInterceptedEvent = null
        }
}
