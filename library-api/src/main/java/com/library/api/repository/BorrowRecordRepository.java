package com.library.api.repository;

import com.library.api.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {
    Optional<BorrowRecord> findByBook_IdAndReturnDateIsNull(Integer bookId);
    List<BorrowRecord> findByReturnDateIsNull();
    List<BorrowRecord> findByMember_Id(Integer memberId);
    List<BorrowRecord> findByMember_IdAndReturnDateIsNull(Integer memberId);
}