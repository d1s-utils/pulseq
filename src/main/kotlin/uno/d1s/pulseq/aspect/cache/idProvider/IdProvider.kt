/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.aspect.cache.idProvider

interface IdProvider {

    fun getId(any: Any): String
}