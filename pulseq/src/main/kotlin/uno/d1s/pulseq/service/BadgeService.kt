package uno.d1s.pulseq.service

interface BadgeService {

    fun createBadge(
        statisticId: String,
        color: String? = null,
        title: String? = null,
        style: String? = null,
        logoUrl: String? = null
    ): ByteArray
}