package uno.d1s.pulseq.util

import org.springframework.util.StringUtils

private const val EMPTY_COLLECTION_MESSAGE = "The collection is empty"

private fun Collection<*>.collectionToDelimitedStringOrMessage(
    delimiter: String, emptyCollectionMessage: String = EMPTY_COLLECTION_MESSAGE
) = if (this.isEmpty()) {
    emptyCollectionMessage
} else {
    StringUtils.collectionToDelimitedString(this, delimiter)
}

fun Collection<*>.toCommaDelimitedString(emptyCollectionMessage: String = EMPTY_COLLECTION_MESSAGE) =
    this.collectionToDelimitedStringOrMessage(", ", emptyCollectionMessage)

fun Collection<*>.toSemicolonDelimitedString(emptyCollectionMessage: String = EMPTY_COLLECTION_MESSAGE) =
    this.collectionToDelimitedStringOrMessage("; ", emptyCollectionMessage)

fun <T> List<T>.nextAfterOrNull(after: T) = this.getOrNull(this.indexOf(after).let { index ->
    if (index == -1) {
        index
    } else {
        index + 1
    }
})

fun <T> List<T>.previousBeforeOrNull(before: T) = this.getOrNull(this.indexOf(before).let { index ->
    if (index == -1) {
        index
    } else {
        index - 1
    }
})

inline fun <T : Any> Collection<T>.forEachPartition(crossinline block: T.(partition: Collection<T>) -> IntRange) {
    val list = this.toList()
    var range: IntRange? = null

    val forEachWithPartition: (collection: Collection<T>) -> Unit = { collection ->
        val thisRange = range

        collection.forEach { el ->
            range = block(el, collection)

            // The range was changed, next loop should be performed over sublist
            if (thisRange != range) {
                return@forEach
            }
        }
    }

    // Iterating over all the collection for the first time
    forEachWithPartition(this)
    range ?: return

    while (range!!.first < range!!.last) {
        forEachWithPartition(
            list.subList(
                range!!.first,
                range!!.last
            )
        )
    }
}