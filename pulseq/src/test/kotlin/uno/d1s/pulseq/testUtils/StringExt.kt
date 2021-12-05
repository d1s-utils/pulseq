package uno.d1s.pulseq.util

import org.junit.jupiter.api.Assertions

fun String.assertNoWhitespace() =
    Assertions.assertTrue(this.split("\\s+".toRegex()).size <= 1)