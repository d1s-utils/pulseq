package uno.d1s.pulseq.controller

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import uno.d1s.pulseq.core.constant.mapping.StatisticMappingConstants
import uno.d1s.pulseq.statistic.Statistic
import javax.validation.constraints.NotEmpty

@Validated
interface StatisticController {

    @GetMapping(StatisticMappingConstants.BASE)
    fun getAllStatistics(): ResponseEntity<List<Statistic>>

    @GetMapping(StatisticMappingConstants.GET_STATISTIC_BY_IDENTIFY)
    fun getStatisticByIdentify(@PathVariable @NotEmpty identify: String): ResponseEntity<Statistic>
}