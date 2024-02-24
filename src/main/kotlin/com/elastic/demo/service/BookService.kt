package com.elastic.demo.service

import com.elastic.demo.model.Book
import com.elastic.demo.model.request.SearchRequest

interface BookService {
    fun create(b: Book): Book
    fun update(id: String, b: Book): Book
    fun delete(id: String)
    fun findById(id: String): Book
    fun findAll(): List<Book>
    fun searchMatchQuery(sr: SearchRequest): List<Book>
    fun searchPhraseQuery(sr: SearchRequest): List<Book>
}