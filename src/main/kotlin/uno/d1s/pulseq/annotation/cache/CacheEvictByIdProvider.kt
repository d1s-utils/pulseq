/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.annotation.cache

import uno.d1s.pulseq.aspect.cache.idProvider.IdProvider
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheEvictByIdProvider(
    val cacheName: String,
    val idProvider: KClass<out IdProvider>
)
