package com.library.api.controller;

import com.library.api.dto.MemberResponse;
import com.library.api.model.Member;
import com.library.api.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public Page<MemberResponse> getAllMembers(@RequestParam int page, @RequestParam int size) {
        return memberService.getAllMembers(page, size);
    }

    @GetMapping("/members/{id}")
    public MemberResponse getMemberById(@PathVariable Integer id){
        return memberService.getMemberById(id);
    }

    @PostMapping("/members")
    public MemberResponse createMember(@Valid @RequestBody Member member){
        return memberService.saveMember(member);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMemberId(@Valid @PathVariable Integer id, @RequestBody Member updated ){
        MemberResponse memberResponse = memberService.updateMemberById(id, updated);
        return ResponseEntity.ok(memberResponse);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMemberById(@PathVariable Integer id){
        memberService.deleteMemberById(id);
        return ResponseEntity.noContent().build();
    }
}
