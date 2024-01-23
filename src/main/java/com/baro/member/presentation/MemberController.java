package com.baro.member.presentation;

import com.baro.auth.domain.AuthMember;
import com.baro.member.application.MemberService;
import com.baro.member.application.dto.GetMemberProfileResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/profile/my")
    public ResponseEntity<GetMemberProfileResult> getMyProfile(
            AuthMember authMember
    ) {
        GetMemberProfileResult result = memberService.getMyProfile(authMember.id());
        return ResponseEntity.ok().body(result);
    }
}
