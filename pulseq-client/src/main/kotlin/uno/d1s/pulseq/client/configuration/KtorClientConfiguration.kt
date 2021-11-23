package uno.d1s.pulseq.client.configuration

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KtorClientConfiguration {

    @Bean
    fun httpClient() = HttpClient(CIO)
}