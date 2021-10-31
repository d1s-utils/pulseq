package uno.d1s.pulseq.util

import java.time.Duration

fun Duration.pretty(): String = buildString {
    val days = this@pretty.toDaysPart()
    val hours = this@pretty.toHoursPart()
    val minutes = this@pretty.toMinutesPart()
    val seconds = this@pretty.toSecondsPart()

    if (days != 0L) append("$days day${grammar(days)}, ")
    if (hours != 0) append("$hours hour${grammar(hours)}, ")
    if (minutes != 0) append("$minutes minute${grammar(minutes)}, ")
    if (seconds != 0) append("$seconds second".grammar(seconds))
}