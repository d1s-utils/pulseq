package uno.d1s.pulseq.client

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PulseqClientApplication

fun main(args: Array<String>) {
    runApplication<PulseqClientApplication>(*args) {
        webApplicationType = WebApplicationType.NONE
    }
}
