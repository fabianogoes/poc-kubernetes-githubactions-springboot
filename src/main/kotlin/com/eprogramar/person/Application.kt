package com.eprogramar.person

import org.bson.types.ObjectId
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@RestController
@RequestMapping("/")
class Welcome(
    private val repository: PersonRepository,
    private val envs: Environment
) {

    init {
        repository.deleteAll()
        listOf(
            Person(name = "Fabiano"),
            Person(name = "Dulci"),
            Person(name = "Leticia"),
            Person(name = "Davi"),
        ).forEach(repository::save)
        repository.findAll().forEach(::println)

        println("MONGO_USER: ${envs.getProperty("MONGO_USER")}")
        println("MONGO_USER: ${envs.getProperty("MONGO_PASSWORD")}")
    }

    @GetMapping
    fun get() = "Welcome to Person Service!"

    @GetMapping("/persons")
    fun getPersons() = ResponseEntity.ok(repository.findAll())

    @PostMapping("/persons")
    fun postPerson(@RequestBody person: Person) = ResponseEntity(repository.save(person), HttpStatus.CREATED)

}

@Document(collection = "persons")
data class Person(
    @Id
    var id: ObjectId = ObjectId.get(),
    var name: String? = null
)

interface PersonRepository : MongoRepository<Person, String>
