package com.baro.member.presentation;

import com.baro.auth.domain.AuthMember;
import com.baro.member.application.MemberService;
import com.baro.member.application.dto.DeleteMemberCommand;
import com.baro.member.application.dto.GetMemberProfileResult;
import com.baro.member.application.dto.UpdateMemberProfileCommand;
import com.baro.member.application.dto.UpdateProfileImageCommand;
import com.baro.member.presentation.dto.DeleteMemberRequest;
import com.baro.member.presentation.dto.UpdateMemberProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/profile/me")
    public ResponseEntity<GetMemberProfileResult> getMyProfile(
            AuthMember authMember
    ) {
        GetMemberProfileResult result = memberService.getMyProfile(authMember.id());
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/profile/me")
    public ResponseEntity<Void> updateMyProfile(
            AuthMember authMember,
            @RequestBody UpdateMemberProfileRequest request
    ) {
        UpdateMemberProfileCommand command = new UpdateMemberProfileCommand(authMember.id(), request.name(),
                request.nickname());
        memberService.updateProfile(command);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/image")
    public ResponseEntity<Void> deleteProfileImage(
            AuthMember authMember
    ) {
        memberService.deleteProfileImage(authMember.id());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/image")
    public ResponseEntity<Void> updateProfileImage(
            AuthMember authMember,
            @RequestBody MultipartFile profileImage
    ) {
        UpdateProfileImageCommand command = new UpdateProfileImageCommand(authMember.id(), profileImage);
        memberService.updateProfileImage(command);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(
            AuthMember authMember,
            @RequestBody DeleteMemberRequest request
    ) {
        DeleteMemberCommand command = new DeleteMemberCommand(authMember.id(), request.reason());
        memberService.deleteMember(command);
        return ResponseEntity.noContent().build();
    }
}
