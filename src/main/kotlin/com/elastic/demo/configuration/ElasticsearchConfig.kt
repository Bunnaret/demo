package com.elastic.demo.configuration

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.TransportUtils
import co.elastic.clients.transport.rest_client.RestClientTransport
import org.apache.http.Header
import org.apache.http.HttpHost
import org.apache.http.message.BasicHeader
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.RestHighLevelClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import java.util.*
import javax.net.ssl.SSLContext

@Configuration
class ElasticsearchConfig {

    @Value("\${certificate.path}")
    lateinit var ca: Resource

    @Value("\${elasticsearch.username}")
    lateinit var username: String

    @Value("\${elasticsearch.password}")
    lateinit var password: String

    @Value("\${elasticsearch.url}")
    lateinit var url: String

    @Value("\${elasticsearch.timeout}")
    private val eTimeout: Int = 0

    @Bean
    fun restClient(): RestClient {
        val sslContext: SSLContext = TransportUtils
            .sslContextFromHttpCaCrt(ca.inputStream)
        val credentialAuth = Base64.getEncoder().encodeToString("$username:$password".toByteArray())
        return RestClient
            .builder(HttpHost.create(url))
            .setDefaultHeaders(
                arrayOf<Header>(
                    BasicHeader("Authorization", "Basic $credentialAuth")
                )
            )
            .setHttpClientConfigCallback { it.setSSLContext(sslContext) }
            .setRequestConfigCallback {
                it.setSocketTimeout(eTimeout)
                    .setConnectTimeout(eTimeout)
            }
            .build()
    }

    @Bean
    fun restHighLevelClient(httpClient: RestClient): RestHighLevelClient = RestHighLevelClientBuilder(httpClient)
        .setApiCompatibilityMode(true)
        .build()

    @Bean
    fun elasticsearchClient(restClient: RestClient): ElasticsearchClient {
        val transport = RestClientTransport(restClient, JacksonJsonpMapper())
        return ElasticsearchClient(transport)
    }

}
