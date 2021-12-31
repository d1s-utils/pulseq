/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.util.time

import uno.d1s.pulseq.util.pluralGrammar
import java.time.Duration
import java.time.Instant

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

fun betweenAbs(start: Instant, end: Instant): Duration = betweenAbs(start, end)