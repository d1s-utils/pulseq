package uno.d1s.pulseq.aspect.cache

import com.github.benmanes.caffeine.cache.Cache
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.annotation.cache.CacheableList
import uno.d1s.pulseq.testUtils.VALID_STUB
import uno.d1s.pulseq.testUtils.testIdProvider
import uno.d1s.pulseq.util.getCacheSafe
import java.lang.reflect.Method
import kotlin.properties.Delegates

@SpringBootTest
@Suppress("UNCHECKED_CAST")
@ContextConfiguration(classes = [CacheableListAspect::class])
internal class CacheableListAspectTest {

    @Autowired
    private lateinit var cacheableListAspect: CacheableListAspect

    @MockkBean
    private lateinit var cacheManager: CacheManager

    @MockkBean
    private lateinit var applicationContext: ApplicationContext

    private val mockProceedingJoinPoint = mockk<ProceedingJoinPoint>(relaxed = true)

    private val mockMethod = mockk<Method>(relaxed = true)

    private val mockCacheableList = mockk<CacheableList>()

    private val mockNativeCache = mockk<Cache<Any, Any>>(relaxed = true)

    @BeforeEach
    fun setup() {
        every {
            (mockProceedingJoinPoint.signature as MethodSignature).method
        } returns mockMethod

        every {
            mockMethod.getAnnotation(CacheableList::class.java)
        } returns mockCacheableList

        every {
            mockCacheableList.cacheName
        } returns VALID_STUB

        every {
            mockCacheableList.idProvider
        } returns testIdProvider::class

        every {
            cacheManager.getCacheSafe(mockCacheableList.cacheName).nativeCache as Cache<Any, Any>
        } returns mockNativeCache

        every {
            mockNativeCache.asMap().values
        } returns mutableListOf(VALID_STUB)

        every {
            mockProceedingJoinPoint.proceed() as Collection<*>
        } returns listOf(VALID_STUB)

        every {
            applicationContext.getBean(mockCacheableList.idProvider.java)
        } returns testIdProvider
    }

    @Test
    fun `should cache the values`() {
        var result: Any by Delegates.notNull()

        assertDoesNotThrow {
            result = cacheableListAspect.doCaching(mockProceedingJoinPoint)
        }

        Assertions.assertEquals(listOf(VALID_STUB), result)

        verify {
            (mockProceedingJoinPoint.signature as MethodSignature).method
        }

        verify {
            cacheManager.getCacheSafe(mockCacheableList.cacheName).nativeCache as Cache<Any, Any>
        }

        verify {
            mockNativeCache.asMap().isEmpty()
        }

        verify {
            mockProceedingJoinPoint.proceed() as Collection<*>
        }

        verify {
            mockNativeCache.put(
                applicationContext.getBean(mockCacheableList.idProvider.java).getId(VALID_STUB),
                VALID_STUB
            )
        }
    }
}