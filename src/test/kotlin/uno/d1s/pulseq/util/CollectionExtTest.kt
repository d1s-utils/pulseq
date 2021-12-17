package uno.d1s.pulseq.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.testUtils.testCollection
import java.util.concurrent.ThreadLocalRandom

@SpringBootTest
@ContextConfiguration(classes = [CollectionExtTest::class])
internal class CollectionExtTest {

    @Test
    fun `should return delimited string from given collection`() {
        Assertions.assertEquals("1.2.3", testCollection.toDelimitedStringOrMessage("."))
    }

    @Test
    fun `should return empty collection message`() {
        Assertions.assertEquals(
            "empty",
            listOf<Any>().toDelimitedStringOrMessage(".", "empty")
        )
    }

    @Test
    fun `should return comma delimited string`() {
        Assertions.assertEquals("1, 2, 3", testCollection.toCommaDelimitedString())
    }

    @Test
    fun `should return semicolon delimited string`() {
        Assertions.assertEquals("1; 2; 3", testCollection.toSemicolonDelimitedString())
    }

    @Test
    fun `should return next element after specified`() {
        Assertions.assertEquals(2, testCollection.nextAfterOrNull(1))
    }

    @Test
    fun `should return previous element before specified`() {
        Assertions.assertEquals(1, testCollection.previousBeforeOrNull(2))
    }

    @Test
    fun `should return null if the element was not found`() {
        Assertions.assertEquals(null, testCollection.getElementFromCurrentIndex(4) {
            it
        })
    }

    @Test
    fun `should return null if the changed index is out of bounds`() {
        Assertions.assertEquals(null, testCollection.getElementFromCurrentIndex(3) {
            it + 1
        })
    }

    @Test
    fun `should return valid page`() {
        Assertions.assertEquals(listOf(testCollection[0]), testCollection.page(0, 1).content)
    }

    @Test
    fun `should iterate for each partition`() {
        assertDoesNotThrow {
            testCollection.forEachPartition {
                if (ThreadLocalRandom.current().nextInt(0, 21) > 10) {
                    testCollection.indexOf(this) + 1..testCollection.size
                } else {
                    testCollection.indexOf(this)..testCollection.size
                }
            }
        }
    }
}