package demo.repository

import com.fasterxml.jackson.databind.ObjectMapper
import demo.model.IdentifiedPerson
import demo.model.Person
import demo.model.jooq.Tables.PERSON
import demo.model.jooq.tables.records.PersonRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class PersonRepository(private val ctx: DSLContext, private val mapper: ObjectMapper) {
    fun getPerson(id: Long) = ctx
                .selectFrom(PERSON)
                .where(PERSON.ID.eq(id))
                .fetchOptional(recordMapper)

    fun getPersons(): List<IdentifiedPerson> = ctx
            .selectFrom(PERSON)
            .fetch(recordMapper)

    fun save(person: Person) = ctx
                .insertInto(PERSON, PERSON.DATA)
                .values(person.toJson())
                .returning(PERSON.ID)
                .fetchOptional()
                .map {
                    IdentifiedPerson(
                        id = it.getValue(PERSON.ID),
                        person = person)
                }
                .orElseThrow { RuntimeException("Cannot get inserted record's id") }

    private fun Person.toJson() = mapper.writer().writeValueAsString(this)

    private val recordMapper: (PersonRecord) -> IdentifiedPerson = {
        val person = mapper.readerFor(Person::class.java).readValue<Person>(it.data)
        IdentifiedPerson(
                id = it.id,
                person = person
        )
    }
}