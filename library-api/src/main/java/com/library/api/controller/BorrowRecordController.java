package com.library.api.controller;

import com.library.api.dto.BorrowRequest;
import com.library.api.dto.BorrowResponse;
import com.library.api.model.BorrowRecord;
import com.library.api.repository.BorrowRecordRepository;
import com.library.api.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/borrow")
public class BorrowRecordController {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BorrowService borrowService;

    public BorrowRecordController(BorrowRecordRepository borrowRecordRepository, BorrowService borrowService) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.borrowService = borrowService;
    }

    @GetMapping("/current")
    public List<BorrowResponse> getAllBorrowedRecords(){
        return borrowService.getCurrentBorrowedBooks();
    }

    @GetMapping("/history/{member_id}")
    public List<BorrowResponse> getBorrowHistory(@PathVariable("member_id") Integer memberId) {
        return borrowService.getBorrowHistory(memberId);
    }

    @PostMapping("/create")
    public ResponseEntity<BorrowResponse> createBorrowRecord(@Valid @RequestBody BorrowRequest borrowRequest){
        BorrowResponse createdBorrowRecord = borrowService.borrowBook(borrowRequest.getBookId(), borrowRequest.getMemberId());
        return ResponseEntity.ok(createdBorrowRecord);
    }

    @PostMapping("/return/{member_id}")
    public ResponseEntity<BorrowResponse> returnBook(@Valid @RequestBody BorrowRequest borrowRequest){
        BorrowResponse returnBorrowedBook = borrowService.returnBook(borrowRequest.getBookId());
        return ResponseEntity.ok(returnBorrowedBook);
    }
}
