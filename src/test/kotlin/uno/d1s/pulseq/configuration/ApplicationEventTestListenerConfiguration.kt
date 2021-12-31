/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.configuration

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import uno.d1s.pulseq.testlistener.ApplicationEventTestListener

@TestConfiguration
internal class ApplicationEventTestListenerConfiguration {

    @Bean
    @Primary
    fun applicationEventTestListener() =
        ApplicationEventTestListener()
}