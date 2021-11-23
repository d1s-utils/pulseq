package uno.d1s.pulseq.util

import uno.d1s.pulseq.core.util.grammar
import java.time.Duration

fun Duration.pretty(): String = buildString {
    val days = this@pretty.toDaysPart()
    val hours = this@pretty.toHoursPart()
    val minutes = this@pretty.toMinutesPart()
    val seconds = this@pretty.toSecondsPart()
    val millis = this@pretty.toMillisPart()

    if (seconds == 0) {
        append("$millis millisecond${grammar(millis)}")
        return@buildString
    }

    if (days != 0L) append("$days day${grammar(days)}, ")
    if (hours != 0) append("$hours hour${grammar(hours)}, ")
    if (minutes != 0) append("$minutes minute${grammar(minutes)}, ")
    append("$seconds second".grammar(seconds))
}