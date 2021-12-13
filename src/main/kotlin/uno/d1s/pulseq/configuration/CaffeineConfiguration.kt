package uno.d1s.pulseq.configuration

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CaffeineConfiguration {

    @Bean
    fun caffeine(): Caffeine<Any, Any> = Caffeine.newBuilder()
        .weakKeys()

    @Bean
    fun cacheManager(): CacheManager = CaffeineCacheManager().apply {
        setCaffeine(caffeine())
    }
}