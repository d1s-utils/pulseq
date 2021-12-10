package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.AntPathMatcher
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import uno.d1s.pulseq.configuration.property.SecurityConfigurationProperties
import uno.d1s.pulseq.core.util.withSlash
import uno.d1s.pulseq.service.AuthenticationService


@Service("authenticationService")
class AuthenticationServiceImpl : AuthenticationService {

    @Autowired
    private lateinit var securityConfigurationProperties: SecurityConfigurationProperties

    override fun isSecuredPath(path: String): Boolean = securityConfigurationProperties.securedPaths.any {
        AntPathMatcher().match(it, path.withSlash())
    }

    override fun validateSecret(secret: String): Boolean = securityConfigurationProperties.secret!! == secret

    override fun isAuthenticatedRequest(): Boolean {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

        (request.getHeader("Authorization") ?: request.getParameter("auth"))?.let {
            if (!this.validateSecret(it)) {
                return true
            }
        } ?: return false
        return true
    }
}