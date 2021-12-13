package uno.d1s.pulseq.strategy.device

sealed class DeviceFindingStrategy(val identify: String) {
    class ByName(name: String) : DeviceFindingStrategy(name)
    class ById(id: String) : DeviceFindingStrategy(id)
    class ByAll(identify: String) : DeviceFindingStrategy(identify)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceFindingStrategy

        if (identify != other.identify) return false

        return true
    }

    override fun hashCode(): Int {
        return identify.hashCode()
    }
}

fun byName(name: String) = DeviceFindingStrategy.ByName(name)
fun byId(id: String) = DeviceFindingStrategy.ById(id)
fun byAll(identify: String) = DeviceFindingStrategy.ByAll(identify)
fun byStrategyType(identify: String, type: DeviceFindingStrategyType) = when (type) {
    DeviceFindingStrategyType.BY_NAME -> DeviceFindingStrategy.ByName(identify)
    DeviceFindingStrategyType.BY_ID -> DeviceFindingStrategy.ById(identify)
    DeviceFindingStrategyType.BY_ALL -> DeviceFindingStrategy.ByAll(identify)
}
