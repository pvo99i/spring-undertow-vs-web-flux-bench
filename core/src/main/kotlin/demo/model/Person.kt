package demo.model

data class Name(val firstName: String, val middleName: String? = null, val lastName: String)

data class Address(val index: String, val city: String, val street: String, val appartment: String)

data class Person(val name: Name, val address: Address)

data class IdentifiedPerson(val id: Long, val person: Person)


