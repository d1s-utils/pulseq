/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.util

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.testUtils.VALID_STUB

@SpringBootTest
@ContextConfiguration(classes = [CacheManagerExtTest::class])
internal class CacheManagerExtTest {

    private val mockCacheManager = mockk<CacheManager>()

    private val mockCache = mockk<Cache>(relaxed = true)

    @BeforeEach
    fun setup() {
        every {
            mockCacheManager.getCache(VALID_STUB)
        } returns mockCache
    }

    @Test
    fun `should return valid cache`() {
        Assertions.assertEquals(mockCache, mockCacheManager.getCacheSafe(VALID_STUB))
    }

    @Test
    fun `should throw an exception on getting cache by invalid name`() {
        every {
            mockCacheManager.getCache(VALID_STUB)
        } returns null

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            mockCacheManager.getCacheSafe(VALID_STUB)
        }
    }
}