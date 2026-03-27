package com.library.api.controller;

import com.library.api.dto.BookResponse;
import com.library.api.model.Book;
import com.library.api.repository.BookRepository;
import com.library.api.service.BookService;
import com.library.api.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public Page<BookResponse> getAllBooks(@RequestParam int page, @RequestParam int size) {
        return bookService.getAllBooks(page, size);
    }

    @GetMapping("/books/{id}")
    public BookResponse getBookById(@PathVariable Integer id) {
         return bookService.getBookById(id);
    }

    @PostMapping("/books")
    public BookResponse createBook(@Valid @RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<BookResponse> updateBookById(@Valid @PathVariable Integer id, @RequestBody Book updated) {
        BookResponse updatedBook = bookService.updateBookById(id, updated);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Integer id) {

        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}

