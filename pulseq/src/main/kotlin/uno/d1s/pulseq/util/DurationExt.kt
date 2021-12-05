package uno.d1s.pulseq.util

import uno.d1s.pulseq.core.util.pluralGrammar
import java.time.Duration

fun Duration.pretty(): String = buildString {
    val days = this@pretty.toDaysPart()
    val hours = this@pretty.toHoursPart()
    val minutes = this@pretty.toMinutesPart()
    val seconds = this@pretty.toSecondsPart()
    val millis = this@pretty.toMillisPart()

    if (seconds == 0) {
        append("$millis millisecond${pluralGrammar(millis)}")
        return@buildString
    }

    if (days != 0L) append("$days day${pluralGrammar(days)}, ")
    if (hours != 0) append("$hours hour${pluralGrammar(hours)}, ")
    if (minutes != 0) append("$minutes minute${pluralGrammar(minutes)}, ")
    append("$seconds second".pluralGrammar(seconds))
}