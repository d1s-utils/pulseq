/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.strategy.source

import uno.d1s.pulseq.util.Identifiable

sealed class SourceFindingStrategy(override val identify: String) : Identifiable {
    class ByName(name: String) : SourceFindingStrategy(name)
    class ById(id: String) : SourceFindingStrategy(id)
    class ByAll(identify: String) : SourceFindingStrategy(identify)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SourceFindingStrategy

        if (identify != other.identify) return false

        return true
    }

    override fun hashCode(): Int {
        return identify.hashCode()
    }
}

fun byName(name: String) = SourceFindingStrategy.ByName(name)
fun byId(id: String) = SourceFindingStrategy.ById(id)
fun byAll(identify: String) = SourceFindingStrategy.ByAll(identify)
fun byStrategyType(identify: String, type: SourceFindingStrategyType) = when (type) {
    SourceFindingStrategyType.BY_NAME -> SourceFindingStrategy.ByName(identify)
    SourceFindingStrategyType.BY_ID -> SourceFindingStrategy.ById(identify)
    SourceFindingStrategyType.BY_ALL -> SourceFindingStrategy.ByAll(identify)
}
