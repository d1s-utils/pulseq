package uno.d1s.pulseq.domain.activity

import liquibase.pro.packaged.T
import java.time.Duration

interface Interval<T> {

    val duration: Duration

    val start: T

    val end: T
}