/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.util

import org.springframework.beans.support.PagedListHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.util.StringUtils

private const val EMPTY_COLLECTION_MESSAGE = "The collection is empty"

fun Collection<*>.toDelimitedStringOrMessage(
    delimiter: String, emptyCollectionMessage: String = EMPTY_COLLECTION_MESSAGE
) = if (this.isEmpty()) {
    emptyCollectionMessage
} else {
    StringUtils.collectionToDelimitedString(this, delimiter)
}

fun Collection<*>.toCommaDelimitedString(emptyCollectionMessage: String = EMPTY_COLLECTION_MESSAGE) =
    this.toDelimitedStringOrMessage(", ", emptyCollectionMessage)

fun Collection<*>.toSemicolonDelimitedString(emptyCollectionMessage: String = EMPTY_COLLECTION_MESSAGE) =
    this.toDelimitedStringOrMessage("; ", emptyCollectionMessage)

fun <T> List<T>.nextAfterOrNull(after: T) = getElementFromCurrentIndex(after) {
    it + 1
}

fun <T> List<T>.previousBeforeOrNull(before: T) = getElementFromCurrentIndex(before) {
    it - 1
}

fun <T> List<T>.getElementFromCurrentIndex(current: T, indexSupplier: (current: Int) -> Int) =
    this.getOrNull(this.indexOf(current).let { index ->
        if (index == -1) {
            index
        } else {
            indexSupplier(index)
        }
    })

fun <T> List<T>.page(page: Int, size: Int): Page<T> = PageImpl(PagedListHolder(this).apply {
    this.pageSize = size
    this.page = page
}.pageList, PageRequest.of(page, size), this.size.toLong())

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