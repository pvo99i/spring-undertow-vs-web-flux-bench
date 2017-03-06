package demo.support

import org.springframework.web.reactive.function.server.RouterDsl
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.Routes
import org.springframework.web.reactive.function.server.ServerResponse

// Copy-pasted from mixit sample project
abstract class RouterFunctionProvider: () -> RouterFunction<ServerResponse> {
    override fun invoke(): RouterFunction<ServerResponse> = RouterDsl().apply(routes).router()

    abstract val routes: Routes

}
