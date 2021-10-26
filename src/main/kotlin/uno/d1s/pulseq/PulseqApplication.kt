package uno.d1s.pulseq

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PulseqApplication

fun main(args: Array<String>) {
    runApplication<PulseqApplication>(*args)
}