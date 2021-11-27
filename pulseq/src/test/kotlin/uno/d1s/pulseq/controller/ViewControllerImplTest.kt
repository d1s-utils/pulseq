package uno.d1s.pulseq.controller

import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import uno.d1s.pulseq.configuration.property.GlobalConfigurationProperties
import uno.d1s.pulseq.configuration.property.ViewConfigurationProperties
import uno.d1s.pulseq.controller.impl.ViewControllerImpl
import uno.d1s.pulseq.service.StatisticService

@ContextConfiguration(classes = [ViewControllerImpl::class])
@WebMvcTest(useDefaultFilters = false, controllers = [ViewControllerImpl::class])
internal class ViewControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean(relaxed = true)
    private lateinit var globalConfigurationProperties: GlobalConfigurationProperties

    @MockkBean(relaxed = true)
    private lateinit var viewConfigurationProperties: ViewConfigurationProperties

    @MockkBean(relaxed = true)
    private lateinit var statisticService: StatisticService

    @Test
    fun `should return valid view on getting the page`() {
        mockMvc.get("/").andExpect {
            model {
                attribute("globalConfigurationProperties", globalConfigurationProperties)
                attribute("themeColor", viewConfigurationProperties.metaThemeColor)
                attribute("statistics", statisticService.getAllStatistics())
                attribute("statisticsFormatted", statisticService.getStatisticsFormatted())
                attributeExists("baseUrl")
            }

            view {
                name("main")
            }

            content {
                contentType("text/html;charset=UTF-8")
            }
        }
    }
}