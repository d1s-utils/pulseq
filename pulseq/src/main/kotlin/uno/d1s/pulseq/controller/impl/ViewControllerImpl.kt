package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import uno.d1s.pulseq.configuration.property.GlobalConfigurationProperties
import uno.d1s.pulseq.configuration.property.ViewConfigurationProperties
import uno.d1s.pulseq.controller.ViewController
import javax.servlet.http.HttpServletRequest

@Controller
@ConditionalOnProperty(prefix = "pulseq.view", name = ["enabled"])
class ViewControllerImpl : ViewController {

    @Autowired
    private lateinit var globalConfigurationProperties: GlobalConfigurationProperties

    @Autowired
    private lateinit var viewConfigurationProperties: ViewConfigurationProperties

    override fun getPage(request: HttpServletRequest): ModelAndView =
        // somehow I can't access this properties inside thymeleaf.
        ModelAndView(
            "main",
            mapOf(
                "globalConfigurationProperties" to globalConfigurationProperties,
                "baseUrl" to ServletUriComponentsBuilder.fromRequestUri(request)
                    .replacePath(null)
                    .build()
                    .toUriString(),
                "themeColor" to viewConfigurationProperties.metaThemeColor
            )
        )
}