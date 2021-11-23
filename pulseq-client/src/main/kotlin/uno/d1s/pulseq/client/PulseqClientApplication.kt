package uno.d1s.pulseq.client

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@ConditionalOnNotWebApplication
class PulseqClientApplication

fun main(args: Array<String>) {
    runApplication<PulseqClientApplication>(*args) {
        webApplicationType = WebApplicationType.NONE
    }
}
