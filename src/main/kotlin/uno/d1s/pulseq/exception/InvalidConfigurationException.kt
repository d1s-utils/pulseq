/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.exception

class InvalidConfigurationException(
    val property: String, val action: String = "Make sure that property $property is present in the configuration."
) : RuntimeException(action)