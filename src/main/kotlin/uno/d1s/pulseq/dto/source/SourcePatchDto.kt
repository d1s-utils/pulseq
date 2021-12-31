/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.dto.source

import javax.validation.constraints.NotEmpty

data class SourcePatchDto(
    @NotEmpty val sourceName: String
)