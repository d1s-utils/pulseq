package uno.d1s.pulseq.domain.activity

interface TypedInterval<T> : Interval<T> {

    val type: IntervalType
}