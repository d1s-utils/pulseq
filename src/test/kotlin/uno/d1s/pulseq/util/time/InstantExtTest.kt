/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.util.time

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import java.time.Instant

@SpringBootTest
@ContextConfiguration(classes = [InstantExtTest::class])
internal class InstantExtTest {

    @Test
    fun `should find closest instant to current`() {
        val validAnswer = Instant.ofEpochSecond(3)

        Assertions.assertEquals(
            validAnswer,
            listOf(
                Instant.ofEpochSecond(1),
                Instant.ofEpochSecond(2),
                validAnswer
            ).findClosestInstantToCurrent().get()
        )
    }

    @Test
    fun `should make instant pretty`() {
        Assertions.assertEquals("January 1, 1970 at 12:00:00 AM UTC", Instant.EPOCH.pretty())
    }
}