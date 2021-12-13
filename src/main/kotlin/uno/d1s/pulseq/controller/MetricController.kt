package uno.d1s.pulseq.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import uno.d1s.pulseq.constant.mapping.MetricMappingConstants
import uno.d1s.pulseq.dto.ErrorDto
import uno.d1s.pulseq.metric.Metric
import javax.validation.constraints.NotEmpty

@Validated
@Tag(name = "Metrics", description = "Get pulseq metrics data.")
interface MetricController {

    @Operation(summary = "Get all metrics.", description = "This operation never returns non-200 codes.")
    @ApiResponses(
        ApiResponse(
            description = "Found metrics", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, array = ArraySchema(
                    schema = Schema(implementation = Metric::class)
                )
            )]
        )
    )
    @GetMapping(MetricMappingConstants.BASE)
    fun getAllMetrics(): ResponseEntity<List<Metric>>

    @Operation(summary = "Get metric by identify.")
    @ApiResponses(
        ApiResponse(
            description = "Found the metric.", responseCode = "200", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = Metric::class)
            )]
        ),
        ApiResponse(
            description = "The metric was not found.", responseCode = "404", content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, schema = Schema(implementation = ErrorDto::class)
            )]
        )
    )
    @GetMapping(MetricMappingConstants.GET_METRIC_BY_IDENTIFY)
    fun getMetricByIdentify(@PathVariable @Parameter(description = "The identify of the metric to search for.") @NotEmpty identify: String): ResponseEntity<Metric>
}