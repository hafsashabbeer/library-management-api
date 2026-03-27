package com.library.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
public class BorrowRequest {

    @NotNull(message = "Book Id Required")
    private Integer bookId;

    @NotNull(message = "Member Id Required")
    private Integer memberId;
}
