/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [ResultExtTest::class])
internal class ResultExtTest {

    @Test
    fun `should return result`() {
        Assertions.assertEquals(
            "ok!",
            runCatching {
                "ok!"
            }.getOrMessage()
        )
    }

    @Test
    fun `should return message`() {
        Assertions.assertEquals(
            "not ok!",
            runCatching {
                throw IllegalStateException("not ok!")
            }.getOrMessage()
        )
    }

    @Test
    fun `should return default message`() {
        Assertions.assertEquals(
            "An error occurred, message is not available.",
            runCatching {
                throw IllegalStateException()
            }.getOrMessage()
        )
    }
}