/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.aspect.cache.idProvider

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.aspect.cache.idProvider.impl.SourceBeatsIdListProvider
import uno.d1s.pulseq.testUtils.testBeats
import uno.d1s.pulseq.testUtils.testSource

@SpringBootTest
@ContextConfiguration(classes = [SourceBeatsIdListProvider::class])
internal class SourceBeatsIdListProviderTest {

    private val sourceBeatsIdListProvider = SourceBeatsIdListProvider()

    @Test
    fun `should return valid list of source beats ids`() {
        Assertions.assertEquals(testBeats.map { it.id!! }, sourceBeatsIdListProvider.getIdList(testSource))
    }
}