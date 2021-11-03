package uno.d1s.pulseq.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PulseqCoreStarterApplication

fun main(args: Array<String>) {
    runApplication<PulseqCoreStarterApplication>(*args)
}
