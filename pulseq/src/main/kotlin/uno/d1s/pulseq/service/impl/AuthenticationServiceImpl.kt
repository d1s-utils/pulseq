package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.AntPathMatcher
import uno.d1s.pulseq.configuration.property.SecurityConfigurationProperties
import uno.d1s.pulseq.service.AuthenticationService
import uno.d1s.pulseq.util.withSlash

@Service("authenticationService")
class AuthenticationServiceImpl : AuthenticationService {

    @Autowired
    private lateinit var securityConfigurationProperties: SecurityConfigurationProperties

    override fun isSecuredPath(path: String): Boolean =
        securityConfigurationProperties.securedPaths.any {
            AntPathMatcher().match(it, path.withSlash())
        }

    override fun validateSecret(secret: String): Boolean =
        securityConfigurationProperties.secret!! == secret
}