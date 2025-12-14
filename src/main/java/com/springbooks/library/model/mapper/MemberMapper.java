package com.springbooks.library.model.mapper;

import com.springbooks.library.model.entity.Member;
import com.springbooks.library.model.request.MemberRequest;
import com.springbooks.library.model.response.MemberResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toEntity(MemberRequest request) {
        if (request == null) {
            return null;
        }
        
        Member member = new Member();
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        
        return member;
    }

    public MemberResponse toResponse(Member member) {
        if (member == null) {
            return null;
        }
        
        return new MemberResponse(
            member.getId(),
            member.getName(),
            member.getEmail()
        );
    }
    
    public void updateEntity(Member member, MemberRequest request) {
        if (member != null && request != null) {
            member.setName(request.getName());
            member.setEmail(request.getEmail());
        }
    }
}
