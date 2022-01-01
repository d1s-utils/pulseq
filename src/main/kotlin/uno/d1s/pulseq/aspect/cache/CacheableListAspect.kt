package uno.d1s.pulseq.aspect.cache

import com.github.benmanes.caffeine.cache.Cache
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import uno.d1s.pulseq.annotation.cache.CacheableList
import uno.d1s.pulseq.util.getCacheSafe

@Aspect
@Component
class CacheableListAspect {

    @Autowired
    private lateinit var cacheManager: CacheManager

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Pointcut("@annotation(uno.d1s.pulseq.annotation.cache.CacheableList)")
    private fun cacheableList() {
    }

    @Around("cacheableList()")
    fun doCaching(proceedingJoinPoint: ProceedingJoinPoint): Any {
        val method = (proceedingJoinPoint.signature as MethodSignature).method
        val annotation = method.getAnnotation(CacheableList::class.java)

        @Suppress("UNCHECKED_CAST")
        val cache = cacheManager.getCacheSafe(annotation.cacheName).nativeCache
                as Cache<Any, Any>

        val cacheMap = cache.asMap()

        return if (cacheMap.isEmpty()) {
            val proceeding = proceedingJoinPoint.proceed() as Collection<*>

            proceeding.forEach {
                cache.put(
                    applicationContext.getBean(annotation.idProvider.java).getId(
                        it ?: throw IllegalArgumentException(
                            "Element of the collection could not be null."
                        )
                    ), it
                )
            }

            proceeding
        } else {
            cacheMap.values
        }
    }
}