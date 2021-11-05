package uno.d1s.pulseq.controller.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.ModelAndView
import uno.d1s.pulseq.configuration.property.GlobalConfigurationProperties
import uno.d1s.pulseq.controller.ViewController

@Controller
@ConditionalOnProperty(prefix = "pulseq.view", name = ["enabled"])
class ViewControllerImpl : ViewController {

    @Autowired
    private lateinit var globalConfigurationProperties: GlobalConfigurationProperties

    override fun getPage(): ModelAndView =
        // somehow I can't access this properties inside thymeleaf.
        ModelAndView("main", mapOf("globalConfigurationProperties" to globalConfigurationProperties))
}