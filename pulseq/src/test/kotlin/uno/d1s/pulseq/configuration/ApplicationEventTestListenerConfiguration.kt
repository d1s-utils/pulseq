package uno.d1s.pulseq.configuration

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import uno.d1s.pulseq.testlistener.ApplicationEventTestListener

@TestConfiguration
class ApplicationEventTestListenerConfiguration {

    @Bean
    @Primary
    fun applicationEventTestListener() =
        ApplicationEventTestListener()
}