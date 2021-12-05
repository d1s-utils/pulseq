package uno.d1s.pulseq.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import uno.d1s.pulseq.configuration.property.GlobalConfigurationProperties
import uno.d1s.pulseq.controller.impl.InfoControllerImpl
import uno.d1s.pulseq.core.constant.mapping.InfoMappingConstants

@ContextConfiguration(classes = [InfoControllerImpl::class])
@WebMvcTest(useDefaultFilters = false, controllers = [InfoControllerImpl::class])
internal class InfoControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean(relaxed = true)
    private lateinit var globalConfigurationProperties: GlobalConfigurationProperties

    @Test
    fun `should return valid properties`() {
        mockMvc.get(InfoMappingConstants.BASE).andExpect {
            status {
                isOk()
            }

            content {
                json(objectMapper.writeValueAsString(globalConfigurationProperties))
            }
        }
    }
}