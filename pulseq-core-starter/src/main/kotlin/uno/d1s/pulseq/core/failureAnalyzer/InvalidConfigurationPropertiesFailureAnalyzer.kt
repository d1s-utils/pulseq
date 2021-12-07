package uno.d1s.pulseq.core.failureAnalyzer

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer
import org.springframework.boot.diagnostics.FailureAnalysis
import uno.d1s.pulseq.core.exception.InvalidConfigurationException

class InvalidConfigurationPropertiesFailureAnalyzer : AbstractFailureAnalyzer<InvalidConfigurationException>() {

    override fun analyze(rootFailure: Throwable, cause: InvalidConfigurationException): FailureAnalysis =
        FailureAnalysis("Property ${cause.property} is not properly configured.", cause.action, cause)
}