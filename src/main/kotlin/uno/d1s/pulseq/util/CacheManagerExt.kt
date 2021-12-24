package uno.d1s.pulseq.util

import org.springframework.cache.Cache
import org.springframework.cache.CacheManager

fun CacheManager.getCacheSafe(name: String): Cache =
    this.getCache(name) ?: throw IllegalArgumentException("The cache with provided name $name does not exists.")