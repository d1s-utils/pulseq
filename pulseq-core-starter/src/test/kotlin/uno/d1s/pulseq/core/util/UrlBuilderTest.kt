package uno.d1s.pulseq.core.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [UrlBuilderTest::class])
internal class UrlBuilderTest {

    @Test
    fun `should build valid url`() {
        Assertions.assertEquals(
            "initial/path/?param=value", UrlBuilder("initial").apply {
                path("path")
                parameter("param", "value")
            }.build()
        )
    }
}