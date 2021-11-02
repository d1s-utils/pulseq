package uno.d1s.pulseq.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

interface ViewController {

    @GetMapping("/")
    fun getPage(): ModelAndView
}