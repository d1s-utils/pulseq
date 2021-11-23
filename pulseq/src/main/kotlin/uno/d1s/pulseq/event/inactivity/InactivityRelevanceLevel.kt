package uno.d1s.pulseq.event.inactivity

enum class InactivityRelevanceLevel(
    val nameString: String,
) {
    COMMON("Common"),
    LONG("Long"),
    WARNING("Warning")
}