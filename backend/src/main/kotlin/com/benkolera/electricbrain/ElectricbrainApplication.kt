package com.benkolera.electricbrain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication(exclude = [
	MongoAutoConfiguration::class,
	MongoDataAutoConfiguration::class
])
class ElectricbrainApplication

fun main(args: Array<String>) {
	runApplication<ElectricbrainApplication>(*args)
}
