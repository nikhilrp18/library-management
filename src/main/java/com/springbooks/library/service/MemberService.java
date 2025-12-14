package com.springbooks.library.service;

import com.springbooks.library.model.request.MemberRequest;
import com.springbooks.library.model.response.MemberResponse;

import java.util.List;

public interface MemberService {

    MemberResponse registerMember(MemberRequest request);

    List<MemberResponse> getAllMembers();

    MemberResponse getMemberById(Long id);

    MemberResponse updateMember(Long id, MemberRequest request);
}
