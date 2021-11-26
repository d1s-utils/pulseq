package uno.d1s.pulseq.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uno.d1s.pulseq.converter.DtoConverter
import uno.d1s.pulseq.converter.impl.BeatDtoConverter
import uno.d1s.pulseq.domain.Beat
import uno.d1s.pulseq.dto.BeatDto

@Configuration
class TestConfiguration {

    @Bean
    fun beatDtoConverter(): DtoConverter<Beat, BeatDto> =
        BeatDtoConverter()
}