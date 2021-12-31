package com.benkolera.electricbrain.services

import com.benkolera.electricbrain.backend.generated.types.Tag
import com.benkolera.electricbrain.backend.generated.types.TagInput
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.constraints.NotEmpty
import org.litote.kmongo.*

@Validated
@ConfigurationProperties("mongodb")
data class TagsServiceConfig(
    @field:NotEmpty var connectionString: String?,
    @field:NotEmpty var dbName: String?
)

data class TagsServiceEnv(val client: MongoClient, val db: MongoDatabase) {
    companion object {
        fun fromConfig(config: TagsServiceConfig): TagsServiceEnv {
            val client = KMongo.createClient(config.connectionString!!)
            val db = client.getDatabase(config.dbName!!)
            return TagsServiceEnv(client, db)
        }
    }
}

@Configuration
class TagsServiceSpringConfig {
    @Bean
    fun tagsServiceEnv(@Autowired config: TagsServiceConfig) = TagsServiceEnv.fromConfig(config)
}

@Service
class TagsService(val env: TagsServiceEnv) {
    fun tagsCol() = env.db.getCollection<Tag>("tags")

    fun listTags(): List<Tag> {
        return tagsCol().find().toList()
    }
    fun addTag(input: TagInput): Tag {
        val tag = Tag(
            UUID.randomUUID().toString(),
            input.name
        )
        tagsCol().insertOne(tag);
       return tag;
    }
}