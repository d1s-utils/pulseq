package uno.d1s.pulseq.configuration

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class CaffeineConfiguration {

    @Bean
    fun caffeine(): Caffeine<Any, Any> = Caffeine.newBuilder()
        .weakKeys()
        .expireAfterWrite(Duration.ofHours(2))
        .maximumSize(250_000)

    @Bean
    fun cacheManager(): CacheManager = CaffeineCacheManager().apply {
        setCaffeine(caffeine())
    }
}