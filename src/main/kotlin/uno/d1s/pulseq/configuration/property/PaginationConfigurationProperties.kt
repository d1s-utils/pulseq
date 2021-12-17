package uno.d1s.pulseq.configuration.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("pulseq.pagination")
class PaginationConfigurationProperties(
    var defaultPageSize: Int = 50
)