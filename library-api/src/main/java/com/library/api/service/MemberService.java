package com.library.api.service;
import com.library.api.dto.MemberResponse;
import com.library.api.exception.MemberDeleteNotAllowedException;
import com.library.api.exception.MemberNotFoundException;
import com.library.api.model.BorrowRecord;
import com.library.api.model.Member;
import com.library.api.repository.BorrowRecordRepository;
import com.library.api.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private BorrowRecordRepository borrowRecordRepository;

    public MemberService(MemberRepository memberRepository, BorrowRecordRepository borrowRecordRepository){
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public MemberResponse mapToMemberResponse(Member member ){
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone()
        );
    }

    public Page<MemberResponse> getAllMembers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> allMembersPage = memberRepository.findAll(pageable);
        return allMembersPage.map(this::mapToMemberResponse);
    }

    public MemberResponse getMemberById(Integer id) {
        Optional<Member> allMembersById = memberRepository.findById(id);

        return allMembersById.stream()
                .map(this::mapToMemberResponse)
                .findAny().orElseThrow(()-> new MemberNotFoundException("Member with member id " + id + " not found"));
    }

    public MemberResponse saveMember(Member member) {
        Member savedMember = memberRepository.save(member);
        return mapToMemberResponse(savedMember);
    }

    public MemberResponse updateMemberById(Integer id, Member updated) {
        return memberRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setEmail(updated.getEmail());
                    existing.setPhone(updated.getPhone());
                    Member savedMember = memberRepository.save(existing);
                    return mapToMemberResponse(savedMember);

                }).orElseThrow(() -> new MemberNotFoundException("Member with member id " + id + " not found"));
    }

    public void deleteMemberById(Integer id) {
        Member memberFound = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member with member id " + id + " not found"));
        List<BorrowRecord> memberBorrowRecord = borrowRecordRepository.findByMember_IdAndReturnDateIsNull(id);
        if (memberBorrowRecord.isEmpty()) {
            memberRepository.deleteById(id);
        } else {
            throw new MemberDeleteNotAllowedException("Delete not allowed. Member with member id " + id + " is currently borrowing books");
        }
    }

}
