package uno.d1s.pulseq.service

import uno.d1s.pulseq.statistic.Statistic

interface BadgeService {

    fun createBadge(statistic: Statistic): Array<Byte>
}