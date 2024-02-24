package com.elastic.demo.service.imp

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders
import com.elastic.demo.model.Book
import com.elastic.demo.model.enum.Indices
import com.elastic.demo.model.request.SearchRequest
import com.elastic.demo.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class BookServiceImp: BookService {

    @Qualifier("elasticsearchClient")
    @Autowired
    lateinit var client: ElasticsearchClient

    override fun create(b: Book): Book {
        return indexDocument(b.id!!, b)
    }

    override fun update(id: String, b: Book): Book {
        return indexDocument(b.id!!, b)
    }

    override fun delete(id: String) {
        client.delete { d ->
            d.index(Indices.BOOK_INDEX.value)
                .id(id)
        }
    }

    override fun findById(id: String): Book {
        val r = client.get(
            { g ->
                g.index(Indices.BOOK_INDEX.value)
                    .id(id)
            }, Book::class.java
        )
        if (r.found()) return r.source()!!
        else throw  ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found")
    }

    override fun findAll(): List<Book> {
        val r = client.search(
            {   s ->
                s.index(Indices.BOOK_INDEX.value)
                s.query(QueryBuilders.matchAll().build()._toQuery())
            }, Book::class.java
        )
        return r.hits().hits().map { it.source()!! }
    }

    override fun searchMatchQuery(sr: SearchRequest): List<Book> {
        val r = client.search(
            {   s ->
                s.index(Indices.BOOK_INDEX.value)
                s.query { q ->
                    q.match { m ->
                        m.field("title")
                        m.query(sr.searchTitle)
                    }
                }
            }, Book::class.java
        )
        return r.hits().hits().map { it.source()!! }
    }

    override fun searchPhraseQuery(sr: SearchRequest): List<Book> {
        val r = client.search(
            {   s ->
                s.index(Indices.BOOK_INDEX.value)
                s.query { q ->
                    q.matchPhrase { m ->
                        m.field("title")
                        m.query(sr.searchTitle)
                    }
                }
            }, Book::class.java
        )
        return r.hits().hits().map { it.source()!! }
    }

    fun indexDocument(id: String, b: Book): Book {
        client.index { i ->
            i.index(Indices.BOOK_INDEX.value)
                .document(b)
                .id(b.id)
        }
        return b
    }


}