/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.strategy

sealed class DomainFindingStrategy {
    class BySource(val sourceId: String) : DomainFindingStrategy()
    class ByHolder(val holderId: String) : DomainFindingStrategy()
    object ByAll : DomainFindingStrategy()
}

fun bySource(identify: String) = DomainFindingStrategy.BySource(identify)
fun byHolder(identify: String) = DomainFindingStrategy.ByHolder(identify)
val byAll = DomainFindingStrategy.ByAll