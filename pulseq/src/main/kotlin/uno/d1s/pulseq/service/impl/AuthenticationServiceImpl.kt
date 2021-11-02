package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.stereotype.Service
import org.springframework.util.AntPathMatcher
import uno.d1s.pulseq.configuration.property.SecurityConfigurationProperties
import uno.d1s.pulseq.service.AuthenticationService

@Service("authenticationService")
class AuthenticationServiceImpl : AuthenticationService {

    @Autowired
    private lateinit var securityConfigurationProperties: SecurityConfigurationProperties

    override fun isSecuredPath(path: String): Boolean =
        securityConfigurationProperties.securedPaths.any {
            AntPathMatcher().match(
                SpelExpressionParser().parseExpression(it).value as String,
                path
            )
        }

    override fun validateSecret(secret: String): Boolean =
        securityConfigurationProperties.secret!! == secret
}