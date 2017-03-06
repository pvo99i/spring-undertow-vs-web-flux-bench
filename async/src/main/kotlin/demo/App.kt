package demo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import demo.support.RouterFunctionProvider
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
open class App {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            SpringApplicationBuilder(App::class.java)
                    .registerShutdownHook(true).run(*args)
        }
    }

    @Bean
    fun routerFunction(routesProvider: List<RouterFunctionProvider>) =
            routesProvider.map { it.invoke() }.reduce(RouterFunction<ServerResponse>::and)

    @Bean
    fun objectMapper() = ObjectMapper().registerKotlinModule()
}
