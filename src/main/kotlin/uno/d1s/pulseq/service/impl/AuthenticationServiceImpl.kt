/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uno.d1s.pulseq.configuration.property.SecurityConfigurationProperties
import uno.d1s.pulseq.service.AuthenticationService
import uno.d1s.pulseq.util.currentRequest


@Service("authenticationService")
class AuthenticationServiceImpl : AuthenticationService {

    @Autowired
    private lateinit var securityConfigurationProperties: SecurityConfigurationProperties

    override fun validateSecret(secret: String): Boolean = securityConfigurationProperties.secret!! == secret

    override fun isAuthenticatedRequest(): Boolean {
        val request = currentRequest

        (request.getHeader("Authorization") ?: request.getParameter("auth"))?.let {
            if (!this.validateSecret(it)) {
                return true
            }
        } ?: return false
        return true
    }
}