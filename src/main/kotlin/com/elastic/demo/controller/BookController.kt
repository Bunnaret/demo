package com.elastic.demo.controller

import com.elastic.demo.model.Book
import com.elastic.demo.model.request.SearchRequest
import com.elastic.demo.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.MergedAnnotations.Search
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book")
class BookController {

    @Autowired
    lateinit var bookService: BookService

    @PostMapping
    fun create(@RequestBody b: Book): Book {
        return bookService.create(b)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable(value = "id") id: String, @RequestBody b: Book): Book {
        return bookService.update(id, b)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable(value = "id") id: String): Book {
        return bookService.findById(id)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") id: String) {
        return bookService.delete(id)
    }

    @GetMapping("/list")
    fun findAll(): List<Book> {
        return bookService.findAll()
    }

    @PostMapping("/search/match-query")
    fun searchMatchQuery(@RequestBody sr: SearchRequest): List<Book> {
        return bookService.searchMatchQuery(sr)
    }

    @PostMapping("/search/phrase-query")
    fun searchPhraseQuery(@RequestBody sr: SearchRequest): List<Book> {
        return bookService.searchPhraseQuery(sr)
    }


}