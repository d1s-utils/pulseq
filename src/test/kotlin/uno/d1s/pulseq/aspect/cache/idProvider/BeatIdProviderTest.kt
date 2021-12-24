package uno.d1s.pulseq.aspect.cache.idProvider

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.aspect.cache.idProvider.impl.BeatIdProvider
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.testUtils.testBeat

@SpringBootTest
@ContextConfiguration(classes = [BeatIdProvider::class])
internal class BeatIdProviderTest {

    private val beatIdProvider = BeatIdProvider()

    @Test
    fun `should return valid beat id`() {
        Assertions.assertEquals(testBeat.id, beatIdProvider.getId(testBeat))
    }

    @Test
    fun `should throw an exception on getting id from a beat that doesnt contain an id`() {
        Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) {
            beatIdProvider.getId(mockk<Beat> {
                every {
                    id
                } returns null
            }
            )
        }
    }
}