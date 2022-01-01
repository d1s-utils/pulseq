/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

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
import uno.d1s.pulseq.annotation.cache.CacheEvictByIdProvider
import uno.d1s.pulseq.util.getCacheSafe

@Aspect
@Component
class CacheEvictByIdProviderAspect {

    @Autowired
    private lateinit var cacheManager: CacheManager

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val logger = LogManager.getLogger()

    @Pointcut("@annotation(uno.d1s.pulseq.annotation.cache.CacheEvictByIdProvider)")
    private fun cacheEvictByIdProvider() {
    }

    @AfterReturning("cacheEvictByIdProvider()")
    fun doCacheEviction(joinPoint: JoinPoint, returnValue: Any?) {
        val method = (joinPoint.signature as MethodSignature).method
        val annotation = method.getAnnotation(CacheEvictByIdProvider::class.java)

        cacheManager.getCacheSafe(annotation.cacheName).evict(
            applicationContext.getBean(annotation.idProvider.java).getId(returnValue
                ?: logger.warn("Method ${method.name} returned the null value, aborting the eviction process.").also {
                    return
                })
        )
    }
}