package demo.sync

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
@ComponentScan(basePackages = arrayOf("demo"))
class App {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            SpringApplicationBuilder(App::class.java)
                    .registerShutdownHook(true).run(*args)
        }
    }
}