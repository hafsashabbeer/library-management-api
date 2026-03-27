package com.library.api.dto;

import lombok.*;

import java.time.Year;

@Getter
@Setter
public class BookResponse {

    private Integer bookId;
    private String title;
    private String author;
    private Integer year;
    private boolean available;

    public BookResponse(Integer bookId, String title, String author, Integer year, boolean available) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.year = year;
        this.available = available;
    }
}
