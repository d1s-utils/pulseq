package uno.d1s.pulseq.service

interface BadgeService {

    fun createBadge(statisticId: String): ByteArray
}