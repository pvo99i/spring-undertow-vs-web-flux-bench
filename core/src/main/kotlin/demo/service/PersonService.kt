package demo.service

import demo.model.Address
import demo.model.IdentifiedPerson
import demo.model.Name
import demo.model.Person
import demo.repository.PersonRepository
import mu.KotlinLogging
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class PersonService(private val repository: PersonRepository) {
    companion object {
        private val log = KotlinLogging.logger { }
    }

    @Cacheable(cacheNames = arrayOf("persons"))
    fun getPerson(id: Long): Optional<IdentifiedPerson> = getNonachedPerson(id)

    fun getNonachedPerson(id: Long) = run {
        log.info { "Requesting person id=$id" }
        repository.getPerson(id)
    }

    fun getPersons(): List<IdentifiedPerson> = run {
        log.info { "Requesting all persons" }
        repository.getPersons()
    }

    fun save(person: Person): IdentifiedPerson = run {
        log.info { "Saving person $person" }
        val result = repository.save(person)
        log.info { "Returning person $result" }
        result
    }

    fun populate() {
        (1..1000).forEach {
            val name = Name("firstName_$it", "middleName_$it", "lastName_$it")
            val address = Address("121212", "Moscow", "Подольских курсантов", "1")
            val person = Person(name = name, address = address)
            repository.save(person)
        }
        log.info { "Test data populated" }
    }
}