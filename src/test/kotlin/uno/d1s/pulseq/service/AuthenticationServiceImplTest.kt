/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.configuration.property.SecurityConfigurationProperties
import uno.d1s.pulseq.service.impl.AuthenticationServiceImpl
import uno.d1s.pulseq.testUtils.INVALID_STUB
import uno.d1s.pulseq.testUtils.VALID_STUB
import uno.d1s.pulseq.util.currentRequest

@SpringBootTest
@ContextConfiguration(classes = [AuthenticationServiceImpl::class])
internal class AuthenticationServiceImplTest {

    @Autowired
    private lateinit var authenticationService: AuthenticationServiceImpl

    @MockkBean
    private lateinit var securityConfigurationProperties: SecurityConfigurationProperties

    @BeforeEach
    fun setup() {
        every {
            securityConfigurationProperties.secret
        } returns VALID_STUB
    }

    @Test
    fun `the secret should be valid`() {
        Assertions.assertTrue(authenticationService.validateSecret(VALID_STUB))
    }

    @Test
    fun `the secret should not be valid`() {
        Assertions.assertFalse(authenticationService.validateSecret(INVALID_STUB))
    }

    @Test
    fun `request should be authenticated`() {
        val mockRequest = MockHttpServletRequest().apply {
            addHeader("Authorization", VALID_STUB)
        }

        mockkStatic("uno.d1s.pulseq.util.CurrentRequestUtilKt") {
            every {
                currentRequest
            } returns mockRequest

            Assertions.assertTrue(
                authenticationService.isAuthenticatedRequest()
            )
        }
    }
}