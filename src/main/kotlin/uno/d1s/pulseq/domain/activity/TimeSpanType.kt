package uno.d1s.pulseq.domain.activity

enum class TimeSpanType {
    INACTIVITY, ACTIVITY;

    fun verb() = if (this == ACTIVITY) {
        "active"
    } else {
        "inactive"
    }
}