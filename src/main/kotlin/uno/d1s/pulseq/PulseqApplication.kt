/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import uno.d1s.pulseq.banner.PulseqSpringBootBanner

@SpringBootApplication
class PulseqApplication

fun main(args: Array<String>) {
    runApplication<PulseqApplication>(*args) {
        setBanner(PulseqSpringBootBanner)
    }
}