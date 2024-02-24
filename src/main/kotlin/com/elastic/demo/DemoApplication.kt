package com.elastic.demo

import com.elastic.demo.model.enum.Indices
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.CreateIndexRequest
import org.elasticsearch.client.indices.GetIndexRequest
import org.elasticsearch.xcontent.XContentType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
import java.nio.file.Files

@SpringBootApplication
class DemoApplication: CommandLineRunner {

	@Qualifier("restHighLevelClient")
	@Autowired
	lateinit var client: RestHighLevelClient

	var log: Log = LogFactory.getLog(DemoApplication::class.java)

	override fun run(vararg args: String?) {
		for (index in Indices.values()) {
			try {
				val indexExists = client.indices().exists(GetIndexRequest(index.value), RequestOptions.DEFAULT)
				if (indexExists) {
					log.info(">>>INDEX ${index.name} IS ALREADY EXIST.")
					continue
				}
				val mapping = loadAsString("mapping/${index.value}.json")
				if(mapping == null) {
					log.info(">>>FAILED TO CREATE INDEX {} ${index.value}")
					continue
				}
				val createIndexRequest = CreateIndexRequest(index.value)
				createIndexRequest.mapping(mapping, XContentType.JSON)

				client.indices().create(createIndexRequest, RequestOptions.DEFAULT)
				log.info(">>>CREATE INDEX ${index.name} SUCCESSFULLY.")
			}catch (ex: Exception) {
				log.error(">>>ERROR INDEX SERVICE {} ${ex.message}")
				break
			}
		}
		log.info(">>> START APPLICATION")
	}

	fun loadAsString(path: String) : String?{
		return try{
			val resource = ClassPathResource(path).file
			String(Files.readAllBytes(resource.toPath()))
		}catch (ex: Exception) {
			log.error(">>>ERROR LOAD FILE {} ${ex.message}")
			null
		}
	}

}


fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
