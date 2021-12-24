package uno.d1s.pulseq.aspect.cache.idProvider

interface IdProvider {

    fun getId(any: Any): String
}