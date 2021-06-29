package com.eprogramar.person

import org.bson.types.ObjectId
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@RestController
@RequestMapping("/")
class Welcome(private val repository: PersonRepository) {

    init {
        repository.deleteAll()
        listOf(
            Person(name = "Fabiano"),
            Person(name = "Dulci"),
            Person(name = "Leticia"),
            Person(name = "Davi"),
        ).forEach(repository::save)
        repository.findAll().forEach(::println)
    }

    @GetMapping
    fun get() = "Welcome to Person Service!"

    @GetMapping("/persons")
    fun getPersons() = ResponseEntity.ok(repository.findAll())

}

@Document(collection = "persons")
data class Person(
    @Id
    var id: ObjectId = ObjectId.get(),
    var name: String? = null
)

interface PersonRepository : MongoRepository<Person, String>
