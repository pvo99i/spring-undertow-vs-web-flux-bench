package demo.sync.controller

import demo.model.IdentifiedPerson
import demo.service.PersonService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.Callable

@RestController
@RequestMapping(produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
class PersonController(val service: PersonService) {
    @GetMapping(path = arrayOf("/person/{id}"))
    fun getPerson(@PathVariable id: Long) = service.getPerson(id)

    @GetMapping(path = arrayOf("/noncached/{id}"))
    fun getPersonNonCached(@PathVariable id: Long): Optional<IdentifiedPerson> = service.getNonachedPerson(id)

    @GetMapping(path = arrayOf("/person/callable/{id}"))
    fun getPersonCallable(@PathVariable id: Long) = Callable {
        getPerson(id)
    }

    @GetMapping(path = arrayOf("/populate/{id}"))
    fun populate(@PathVariable id: Long) = service.populate()
}