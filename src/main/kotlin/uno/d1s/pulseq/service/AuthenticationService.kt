package uno.d1s.pulseq.service

interface AuthenticationService {

    fun isSecuredPath(path: String): Boolean

    fun validateSecret(secret: String): Boolean
}