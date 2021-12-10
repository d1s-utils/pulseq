package uno.d1s.pulseq.service

interface AuthenticationService {

    fun validateSecret(secret: String): Boolean

    fun isAuthenticatedRequest(): Boolean
}