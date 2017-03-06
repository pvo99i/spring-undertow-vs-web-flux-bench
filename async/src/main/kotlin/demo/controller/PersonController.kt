package demo.controller

import demo.model.IdentifiedPerson
import demo.service.PersonService
import demo.support.RouterFunctionProvider
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_STREAM_JSON
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.Routes
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

@Controller
class PersonController(private val service: PersonService) : RouterFunctionProvider() {

    override val routes: Routes = {
        accept(APPLICATION_STREAM_JSON).route {
            "/noncached".route {
                GET("/{id}", this@PersonController::getNoncachedPerson)
            }

            "/person".route {
                GET("/{id}", this@PersonController::getPerson)
            }
        }
        "/populate/".route {
            GET("/{id}", this@PersonController::populate)
        }
    }

    fun getPerson(id: Long): Mono<IdentifiedPerson> = Mono.just(
            service.getPerson(id).orElseThrow { RuntimeException("Person $id not found.") }
    )

    fun getNoncachedPerson(id: Long): Mono<IdentifiedPerson> = Mono.just(
            service.getNonachedPerson(id).orElseThrow { RuntimeException("Person $id not found.") }
    )


    fun getPerson(req: ServerRequest): Mono<ServerResponse> = ok()
            .json()
            .body(
                    getPerson(req.pathVariable("id").toLong())
            )

    fun populate(req: ServerRequest): Mono<ServerResponse> = ok().json().body(
            Mono.just(service.populate())
    )

    fun getNoncachedPerson(req: ServerRequest): Mono<ServerResponse> = ok()
            .json()
            .body(
                    getNoncachedPerson(req.pathVariable("id").toLong())
            )
}

private fun ServerResponse.BodyBuilder.json() = contentType(MediaType.APPLICATION_STREAM_JSON)