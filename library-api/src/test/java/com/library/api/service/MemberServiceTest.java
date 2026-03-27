package com.library.api.service;

import com.library.api.exception.MemberDeleteNotAllowedException;
import com.library.api.model.Book;
import com.library.api.model.BorrowRecord;
import com.library.api.model.Member;
import com.library.api.repository.BorrowRecordRepository;
import com.library.api.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void deleteMember_withActiveBorrow(){
        Member member = new Member();
        member.setId(1);

        Book book = new Book();
        book.setId(1);
        book.setAvailable(false);

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setId(1);
        borrowRecord.setMember(member);
        borrowRecord.setBook(book);
        borrowRecord.setReturnDate(null);

        when(memberRepository.findById(1)).thenReturn(Optional.of(member));
        when(borrowRecordRepository.findByMember_IdAndReturnDateIsNull(1)).thenReturn(List.of(borrowRecord));

        MemberDeleteNotAllowedException ex = assertThrows(
                MemberDeleteNotAllowedException.class,
                () -> memberService.deleteMemberById(1)
        );

        assertEquals("Delete not allowed. Member with member id 1 is currently borrowing books", ex.getMessage());

        verify(memberRepository, never()).save(any(Member.class));

    }
}
