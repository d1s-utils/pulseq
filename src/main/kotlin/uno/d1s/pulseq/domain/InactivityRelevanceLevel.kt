package uno.d1s.pulseq.domain

enum class InactivityRelevanceLevel(
    val nameString: String,
) {
    COMMON("Common"),
    LONG("Long"),
    WARNING("Warning")
}