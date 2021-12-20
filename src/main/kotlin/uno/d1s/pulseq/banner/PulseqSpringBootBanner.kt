/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.banner

import org.springframework.boot.Banner
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import java.io.PrintStream

object PulseqSpringBootBanner : Banner {

    override fun printBanner(environment: Environment, sourceClass: Class<*>, out: PrintStream) {
        out.println(
            """
                 _                
     _ __  _   _| |___  ___  __ _ 
    | '_ \| | | | / __|/ _ \/ _` | 
    | |_) | |_| | \__ \  __/ (_| |
    | .__/ \__,_|_|___/\___|\__, |
    |_|                        |_|
                
    Welcome to Pulseq. 
    Release Version: ${environment["pulseq.version"]}
    Repository: ${environment["pulseq.repository"]}
                
            """
        )
    }
}