package uno.d1s.pulseq.aspect.cache

import org.apache.logging.log4j.LogManager
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import uno.d1s.pulseq.annotation.cache.CacheEvictByListIdProvider
import uno.d1s.pulseq.util.getCacheSafe

@Aspect
@Component
internal class CacheEvictByListIdProviderAspect {

    @Autowired
    private lateinit var cacheManager: CacheManager

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val logger = LogManager.getLogger()

    @Pointcut("@annotation(uno.d1s.pulseq.annotation.cache.CacheEvictByListIdProvider)")
    private fun cacheEvictByListIdProvider() {
    }

    @AfterReturning("cacheEvictByListIdProvider()", returning = "returnValue")
    fun doCacheEviction(joinPoint: JoinPoint, returnValue: Any?) {
        val method = (joinPoint.signature as MethodSignature).method

        val annotation = method.getAnnotation(CacheEvictByListIdProvider::class.java)

        applicationContext.getBean(annotation.idListProvider.java).getIdList(returnValue
            ?: logger.warn("Method ${method.name} returned the null value, aborting the eviction process.")
                .also {
                    return
                }).forEach {
            cacheManager.getCacheSafe(annotation.cacheName).evict(it)
        }
    }
}