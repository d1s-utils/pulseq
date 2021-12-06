package uno.d1s.pulseq.filter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import uno.d1s.pulseq.service.AuthenticationService
import uno.d1s.pulseq.util.HttpServletResponseUtil
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationRequestFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @Autowired
    private lateinit var httpServletResponseUtil: HttpServletResponseUtil

    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        if (authenticationService.isSecuredPath(request.requestURI)) {
            (request.getHeader("Authorization") ?: request.getParameter("auth"))?.let {
                if (!authenticationService.validateSecret(it)) {
                    response.sendUnauthorized("Authentication data is invalid.")
                    return
                }
            } ?: response.sendUnauthorized("Authentication is required.").also {
                return
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun HttpServletResponse.sendUnauthorized(message: String) {
        httpServletResponseUtil.sendErrorDto(this) {
            status = HttpStatus.UNAUTHORIZED.value()
            this.message = message
        }
    }
}