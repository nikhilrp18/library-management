package com.springbooks.library.service.impl;

import com.springbooks.library.exception.DuplicateResourceException;
import com.springbooks.library.exception.MemberNotFoundException;
import com.springbooks.library.model.entity.Member;
import com.springbooks.library.model.mapper.MemberMapper;
import com.springbooks.library.model.request.MemberRequest;
import com.springbooks.library.model.response.MemberResponse;
import com.springbooks.library.repository.MemberRepository;
import com.springbooks.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberMapper memberMapper;

    
    @Override
    public MemberResponse registerMember(MemberRequest request) {
        log.debug("Registering member with email: {}", request.getEmail());
        
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Member with email " + request.getEmail() + " already exists");
        }
        
        Member member = memberMapper.toEntity(request);
        Member savedMember = memberRepository.save(member);
        
        log.info("Member registered successfully with ID: {}", savedMember.getId());
        return memberMapper.toResponse(savedMember);
    }


    @Override
    @Transactional(readOnly = true)
    public List<MemberResponse> getAllMembers() {
        log.debug("Fetching all members");
        
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(memberMapper::toResponse)
                .collect(Collectors.toList());
    }

        
    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMemberById(Long id) {
        log.debug("Fetching member with ID: {}", id);
        
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with ID: " + id));
        
        return memberMapper.toResponse(member);
    }

    @Override
    public MemberResponse updateMember(Long id, MemberRequest request) {
        log.debug("Updating member with ID: {}", id);
        
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with ID: " + id));
        
        if (!existingMember.getEmail().equals(request.getEmail()) && 
            memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Member with email " + request.getEmail() + " already exists");
        }
        
        memberMapper.updateEntity(existingMember, request);
        Member updatedMember = memberRepository.save(existingMember);
        
        log.info("Member updated successfully with ID: {}", updatedMember.getId());
        return memberMapper.toResponse(updatedMember);
    }
}
