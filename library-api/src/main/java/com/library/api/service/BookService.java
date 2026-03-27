package com.library.api.service;

import com.library.api.dto.BookResponse;
import com.library.api.exception.BookDeleteNotAllowedException;
import com.library.api.exception.BookNotFoundException;
import com.library.api.model.Book;
import com.library.api.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    private BookResponse mapToBookResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear(),
                book.getAvailable()
        );
    }

    public Page<BookResponse> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book>  allBooksPage = bookRepository.findAll(pageable);

        return allBooksPage.map(this::mapToBookResponse);

    }

    public BookResponse getBookById(Integer id) {
        Optional<Book> allBooksById = bookRepository.findById(id);

        return allBooksById.stream()
                .map(this::mapToBookResponse)
                .findAny().orElseThrow(()-> new BookNotFoundException("Book with book id " + id + " not found"));
    }

    public BookResponse saveBook(Book book) {
        Book savedBook = bookRepository.save(book);
        return mapToBookResponse(savedBook);
    }

    public BookResponse updateBookById(Integer id, Book updated) {
        return bookRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(updated.getTitle());
                    existing.setAuthor(updated.getAuthor());
                    existing.setYear(updated.getYear());
                    existing.setAvailable(updated.getAvailable());
                    Book savedBook = bookRepository.save(existing);
                    return mapToBookResponse(savedBook);

                }).orElseThrow(() -> new BookNotFoundException("Book with book id " + id + " not found"));
    }

    public void deleteBookById(Integer id) {
        Book bookFound = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with book id " + id + " not found"));
        if (bookFound.getAvailable()) {
            bookRepository.deleteById(id);

        } else {
            throw new BookDeleteNotAllowedException("Delete not allowed. Book with book id " + id + " is currently borrowed");
        }
    }
}
