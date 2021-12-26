package uno.d1s.pulseq.aspect.cache.idProvider.impl

import org.springframework.stereotype.Component
import uno.d1s.pulseq.aspect.cache.idProvider.IdProvider
import uno.d1s.pulseq.domain.Beat

@Component
class BeatIdProvider : IdProvider {

    override fun getId(any: Any): String =
        (any as Beat).id ?: throw IllegalArgumentException("Id is not present.")
}