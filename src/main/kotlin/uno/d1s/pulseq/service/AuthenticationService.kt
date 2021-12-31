/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.service

interface AuthenticationService {

    fun validateSecret(secret: String): Boolean

    fun isAuthenticatedRequest(): Boolean
}