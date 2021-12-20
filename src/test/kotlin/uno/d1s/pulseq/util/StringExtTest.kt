/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [StringExtTest::class])
internal class StringExtTest {

    @Test
    fun `should return grammatically correct string`() {
        Assertions.assertEquals("things", "thing".pluralGrammar(2))
        Assertions.assertEquals("thing", "thing".pluralGrammar(1))
    }

    @Test
    fun `should return grammatically correct character for plurals`() {
        Assertions.assertEquals("s", pluralGrammar(2))
        Assertions.assertEquals("", pluralGrammar(1))
    }

    @Test
    fun `should return string with the slash at the end`() {
        Assertions.assertEquals("ok/", "ok".withSlash())
        Assertions.assertEquals("ok/", "ok/".withSlash())
    }

    @Test
    fun `should replace the placeholder`() {
        Assertions.assertEquals(
            "it's ok",
            "{placeholder} ok".replacePathPlaceholder(
                "placeholder",
                "it's"
            )
        )
    }
}