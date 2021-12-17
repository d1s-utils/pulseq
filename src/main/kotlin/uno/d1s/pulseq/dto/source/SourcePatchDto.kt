package uno.d1s.pulseq.dto.source

import javax.validation.constraints.NotEmpty

data class SourcePatchDto(
    @NotEmpty val sourceName: String
)