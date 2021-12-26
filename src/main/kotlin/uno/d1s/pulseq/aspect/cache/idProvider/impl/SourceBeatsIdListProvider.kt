package uno.d1s.pulseq.aspect.cache.idProvider.impl

import org.springframework.stereotype.Component
import uno.d1s.pulseq.aspect.cache.idProvider.IdListProvider
import uno.d1s.pulseq.domain.Source

@Component
class SourceBeatsIdListProvider : IdListProvider {

    override fun getIdList(any: Any): List<String> =
        (any as Source).beats!!.map {
            it.id!!
        }
}