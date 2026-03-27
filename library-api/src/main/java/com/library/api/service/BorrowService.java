package com.library.api.service;

import com.library.api.dto.BorrowResponse;
import com.library.api.exception.BookNotAvailableException;
import com.library.api.exception.BookNotFoundException;
import com.library.api.exception.BorrowRecordNotFoundException;
import com.library.api.exception.MemberNotFoundException;
import com.library.api.model.Book;
import com.library.api.model.BorrowRecord;
import com.library.api.model.Member;
import com.library.api.repository.BookRepository;
import com.library.api.repository.BorrowRecordRepository;
import com.library.api.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {

    BookRepository bookRepository;
    MemberRepository memberRepository;
    BorrowRecordRepository borrowRecordRepository;

    BorrowService(BookRepository bookRepository, MemberRepository memberRepository, BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    private BorrowResponse mapToBorrowResponse(BorrowRecord record) {

        return new BorrowResponse(
                record.getId(),
                record.getBook().getId(),
                record.getBook().getTitle(),
                record.getMember().getId(),
                record.getMember().getName(),
                record.getBorrowDate(),
                record.getReturnDate()
        );
    }

    public List<BorrowResponse> getCurrentBorrowedBooks() {

        List<BorrowRecord> records = borrowRecordRepository.findByReturnDateIsNull();

        return records.stream()
                .map(this::mapToBorrowResponse)
                .toList();
    }

    public List<BorrowResponse> getBorrowHistory(Integer memberId) {

        List<BorrowRecord> records = borrowRecordRepository.findByMember_Id(memberId);

        return records.stream()
                .map(this::mapToBorrowResponse)
                .toList();
    }

    @Transactional
    public BorrowResponse borrowBook(Integer bookId, Integer memberId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + bookId + " not found"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with id " + memberId + " not found"));

        if (!book.getAvailable()) {
            throw new BookNotAvailableException("Book with id " + bookId + " not available to borrow");
        }

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(book);
        borrowRecord.setMember(member);
        borrowRecord.setBorrowDate(LocalDate.now());
        borrowRecord.setReturnDate(null);

        BorrowRecord savedBorrowRecord = borrowRecordRepository.save(borrowRecord);

        book.setAvailable(false);
        bookRepository.save(book);


        return new BorrowResponse(savedBorrowRecord.getId(), savedBorrowRecord.getBook().getId(),savedBorrowRecord.getBook().getTitle(), savedBorrowRecord.getMember().getId(), savedBorrowRecord.getMember().getName(), savedBorrowRecord.getBorrowDate(),savedBorrowRecord.getReturnDate());
    }

    @Transactional
    public BorrowResponse returnBook(Integer bookId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findByBook_IdAndReturnDateIsNull(bookId)
                .orElseThrow(() -> new BorrowRecordNotFoundException("Borrow Record with book id " + bookId + " not found"));


        borrowRecord.setReturnDate(LocalDate.now());
        borrowRecord.getBook().setAvailable(true);

        BorrowRecord savedBorrowRecord = borrowRecordRepository.save(borrowRecord);
        bookRepository.save(borrowRecord.getBook());

        return new BorrowResponse(savedBorrowRecord.getId(), savedBorrowRecord.getBook().getId(),savedBorrowRecord.getBook().getTitle(), savedBorrowRecord.getMember().getId(), savedBorrowRecord.getMember().getName(), savedBorrowRecord.getBorrowDate(),savedBorrowRecord.getReturnDate());

    }
}
