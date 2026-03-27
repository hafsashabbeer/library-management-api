package com.library.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BorrowResponse {
    private Integer borrowId;
    private Integer bookId;
    private String bookTitle;
    private Integer memberId;
    private String memberName;
    private LocalDate borrowDate;
    private LocalDate returnDate;


    public BorrowResponse(Integer borrowId, Integer bookId, String bookTitle, Integer memberId, String memberName, LocalDate borrowDate, LocalDate returnDate) {
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.memberId = memberId;
        this.memberName = memberName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;

    }
}