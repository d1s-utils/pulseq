package uno.d1s.pulseq.aspect.cache.idProvider

interface IdListProvider {

    fun getIdList(any: Any): List<String>
}