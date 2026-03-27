package com.library.api.service;

import com.library.api.dto.BorrowResponse;
import com.library.api.exception.BookNotAvailableException;
import com.library.api.exception.BookNotFoundException;
import com.library.api.exception.BorrowRecordNotFoundException;
import com.library.api.model.Book;
import com.library.api.model.BorrowRecord;
import com.library.api.model.Member;
import com.library.api.repository.BookRepository;
import com.library.api.repository.BorrowRecordRepository;
import com.library.api.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowServiceTest {

    @Mock
    private BorrowRecordRepository borrowRecordRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private BorrowService borrowService;

    @Test
    void borrowBook_success(){
        Book book = new Book();
        book.setId(1);
        book.setAvailable(true);

        Member member = new Member();
        member.setId(1);

        BorrowRecord borrowRecord = new BorrowRecord();

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1)).thenReturn(Optional.of(member));
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(borrowRecord);

        BorrowResponse result = borrowService.borrowBook(1,1);

        assertNotNull(result);
        assertFalse(book.getAvailable());
        verify(borrowRecordRepository).save(any(BorrowRecord.class));
        verify(bookRepository).save(book);

    }

    @Test
    void borrowBook_bookUnavailable(){
        Book book = new Book();
        book.setId(1);
        book.setAvailable(false);

        Member member = new Member();
        member.setId(1);

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1)).thenReturn(Optional.of(member));

        BookNotAvailableException ex = assertThrows(
                BookNotAvailableException.class,
                () -> borrowService.borrowBook(1, 1)
        );

        assertEquals("Book with id 1 not available to borrow", ex.getMessage());

        verify(borrowRecordRepository, never()).save(any(BorrowRecord.class));
    }

    @Test
    void borrowBook_bookNotFound(){

        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        BookNotFoundException ex = assertThrows(
                BookNotFoundException.class,
                () -> borrowService.borrowBook(1,1)
        );

        assertEquals("Book with id 1 not found", ex.getMessage());

        verify(memberRepository, never()).findById(anyInt());
        verify(borrowRecordRepository, never()).save(any(BorrowRecord.class));

    }

    @Test
    void returnBook_success(){
        Book existingBook = new Book();
        existingBook.setId(1);
        existingBook.setAvailable(false);

        Member member = new Member();
        member.setId(1);

        Book updatedBook = new Book();
        updatedBook.setId(1);
        updatedBook.setAvailable(true);

        BorrowRecord existingBorrowRecord = new BorrowRecord();
        existingBorrowRecord.setId(1);
        existingBorrowRecord.setReturnDate(null);
        existingBorrowRecord.setBook(existingBook);
        existingBorrowRecord.setMember(member);

        BorrowRecord updatedBorrowRecord = new BorrowRecord();
        updatedBorrowRecord.setId(1);
        updatedBorrowRecord.setReturnDate(LocalDate.now());
        updatedBorrowRecord.setBook(updatedBook);
        updatedBorrowRecord.setMember(member);

        when(borrowRecordRepository.findByBook_IdAndReturnDateIsNull(1)).thenReturn(Optional.of(existingBorrowRecord));
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);
        when(borrowRecordRepository.save(existingBorrowRecord)).thenReturn(updatedBorrowRecord);

        BorrowResponse result = borrowService.returnBook(1);

        assertNotNull(result);
        assertEquals(LocalDate.now(),result.getReturnDate());
        assertTrue(existingBook.getAvailable());

        verify(bookRepository).save(existingBook);
        verify(borrowRecordRepository).save(existingBorrowRecord);

    }

    @Test
    void returnBook_noActiveBorrow(){

        when(borrowRecordRepository.findByBook_IdAndReturnDateIsNull(1)).thenReturn(Optional.empty());

        BorrowRecordNotFoundException ex = assertThrows(
                BorrowRecordNotFoundException.class,
                () -> borrowService.returnBook(1)
        );

        assertEquals("Borrow Record with book id 1 not found", ex.getMessage());

        verify(borrowRecordRepository, never()).save(any(BorrowRecord.class));
    }
}
