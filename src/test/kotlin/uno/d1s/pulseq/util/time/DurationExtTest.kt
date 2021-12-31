/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.util.time

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.util.pretty
import java.time.Duration

@SpringBootTest
@ContextConfiguration(classes = [DurationExtTest::class])
internal class DurationExtTest {

    @Test
    fun `should make duration pretty`() {
        Assertions.assertEquals("1 minute, 14 seconds", Duration.ofSeconds(74).pretty())
        Assertions.assertEquals("2 minutes, 1 second", Duration.ofSeconds(121).pretty())
    }
}