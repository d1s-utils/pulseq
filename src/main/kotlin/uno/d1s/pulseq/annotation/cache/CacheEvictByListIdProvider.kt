package uno.d1s.pulseq.annotation.cache

import uno.d1s.pulseq.aspect.cache.idProvider.IdListProvider
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheEvictByListIdProvider(
    val cacheName: String,
    val idListProvider: KClass<out IdListProvider>
)

