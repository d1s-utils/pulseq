/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun List<Instant>.findClosestInstantToCurrent(): Optional<Instant> =
    Optional.ofNullable(
        this.filter {
            Instant.now() >= it
        }.maxOrNull()
    )

fun Instant.pretty(): String =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
        .withZone(ZoneId.of("UTC")).format(this)
