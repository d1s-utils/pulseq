package uno.d1s.pulseq.metric

import uno.d1s.pulseq.util.Identifiable

interface Metric : Identifiable {
    val title: String
    val description: String
    val shortDescription: String
}