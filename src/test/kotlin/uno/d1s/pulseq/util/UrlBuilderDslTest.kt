package uno.d1s.pulseq.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [UrlBuilderDslTest::class])
internal class UrlBuilderDslTest {

    @Test
    fun `should build valid url using dsl`() {
        Assertions.assertEquals("initial/path/?param=value", buildUrl("initial") {
            path("path")
            parameter("param", "value")
        })
    }
}