package uno.d1s.pulseq.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest

interface ViewController {

    @GetMapping("/")
    fun getPage(request: HttpServletRequest): ModelAndView
}