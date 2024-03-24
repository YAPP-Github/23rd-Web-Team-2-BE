package com.baro.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.archive.fake.FakeArchiveRepository;
import com.baro.common.fake.FakeImageStorageClient;
import com.baro.common.image.ImageStorageClient;
import com.baro.member.application.dto.DeleteMemberCommand;
import com.baro.member.application.dto.GetMemberProfileResult;
import com.baro.member.application.dto.UpdateMemberProfileCommand;
import com.baro.member.application.dto.UpdateProfileImageCommand;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fake.FakeMemberWithdrawalRepository;
import com.baro.member.fixture.MemberFixture;
import com.baro.memo.fake.FakeTemporalMemoRepository;
import com.baro.memofolder.fake.FakeMemoFolderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberServiceTest {

    private MemberService memberService;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        FakeArchiveRepository archiveRepository = new FakeArchiveRepository();
        FakeTemporalMemoRepository temporalMemoRepository = new FakeTemporalMemoRepository();
        FakeMemoFolderRepository memoFolderRepository = new FakeMemoFolderRepository();
        ImageStorageClient imageStorageClient = new FakeImageStorageClient();
        FakeMemberWithdrawalRepository memberWithdrawalRepository = new FakeMemberWithdrawalRepository();

        memberService = new MemberService(memberRepository, imageStorageClient, temporalMemoRepository,
                archiveRepository, memoFolderRepository, memberWithdrawalRepository);
    }

    @Test
    void 사용자_프로필_조회() {
        // given
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("바로"));

        // when
        GetMemberProfileResult result = memberService.getMyProfile(savedMember.getId());

        // then
        assertThat(result.nickname()).isEqualTo("바로");
    }

    @Test
    void 존재하지_않는_사용자_조회시_예외_발생() {
        // given
        Long nonExistMemberId = 1L;

        // when & then
        assertThatThrownBy(() -> memberService.getMyProfile(nonExistMemberId))
                .isInstanceOf(MemberException.class)
                .extracting("exceptionType")
                .isEqualTo(MemberExceptionType.NOT_EXIST_MEMBER);
    }

    @Test
    void 사용자_프로필_수정() {
        // given
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        UpdateMemberProfileCommand command = new UpdateMemberProfileCommand(
                savedMember.getId(),
                "바로",
                "바로닉네임"
        );

        // when
        memberService.updateProfile(command);

        // then

        assertThat(savedMember.getNickname().value()).isEqualTo("바로닉네임");
    }

    @Test
    void 사용자_프로필_수정_닉네임_중복_예외_발생() {
        // given
        memberRepository.save(MemberFixture.memberWithNickname("바로닉네임"));
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("이전닉네임"));
        UpdateMemberProfileCommand command = new UpdateMemberProfileCommand(
                savedMember.getId(),
                "바로",
                "바로닉네임"
        );
        memberRepository.save(MemberFixture.memberWithNickname("바로닉네임"));

        // when & then
        assertThatThrownBy(() -> memberService.updateProfile(command))
                .isInstanceOf(MemberException.class)
                .extracting("exceptionType")
                .isEqualTo(MemberExceptionType.NICKNAME_DUPLICATION);
    }

    @Test
    void 존재하지_않는_사용자_프로필_수정시_예외_발생() {
        // given
        Long nonExistMemberId = 1L;
        UpdateMemberProfileCommand command = new UpdateMemberProfileCommand(
                nonExistMemberId,
                "바로",
                "바로닉네임"
        );

        // when & then
        assertThatThrownBy(() -> memberService.updateProfile(command))
                .isInstanceOf(MemberException.class)
                .extracting("exceptionType")
                .isEqualTo(MemberExceptionType.NOT_EXIST_MEMBER);
    }

    @Test
    void 사용자_프로필_수정_닉네임_최대_길이_초과시_예외_발생() {
        // given
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        UpdateMemberProfileCommand command = new UpdateMemberProfileCommand(
                savedMember.getId(),
                "바로",
                "바로닉네임".repeat(10)
        );

        // when & then
        assertThatThrownBy(() -> memberService.updateProfile(command))
                .isInstanceOf(MemberException.class)
                .extracting("exceptionType")
                .isEqualTo(MemberExceptionType.OVER_MAX_SIZE_NICKNAME);
    }

    @Test
    void 사용자_프로필_수정시_닉네임이_비어있으면_예외_발생() {
        // given
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        UpdateMemberProfileCommand command = new UpdateMemberProfileCommand(
                savedMember.getId(),
                "바로",
                ""
        );

        // when & then
        assertThatThrownBy(() -> memberService.updateProfile(command))
                .isInstanceOf(MemberException.class)
                .extracting("exceptionType")
                .isEqualTo(MemberExceptionType.EMPTY_NICKNAME);
    }

    @Test
    void 사용자_프로필_이미지_삭제() {
        // given
        String profileImageUrl = "profile-image-path";
        Member member = MemberFixture.memberWithNicknameAndProfileImage("바로", profileImageUrl);
        Member savedMember = memberRepository.save(member);

        // when
        memberService.deleteProfileImage(savedMember.getId());

        // then
        assertThat(savedMember.getProfileImageUrl()).isNull();
    }

    @Test
    void 사용자_프로필_이미지_삭제시_기본_이미지인_경우_예외_발생() {
        // given
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("바로"));

        // when & then
        assertThatThrownBy(() -> memberService.deleteProfileImage(savedMember.getId()))
                .isInstanceOf(MemberException.class)
                .extracting("exceptionType")
                .isEqualTo(MemberExceptionType.NOT_EXIST_PROFILE_IMAGE);
    }

    @Test
    void 사용자_프로필_이미지_수정() {
        // given
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        UpdateProfileImageCommand command = new UpdateProfileImageCommand(
                savedMember.getId(),
                new MockMultipartFile("profile-image", "profile-image.png", "image/png", "profile-image".getBytes())
        );

        // when
        memberService.updateProfileImage(command);

        // then
        assertThat(savedMember.getProfileImageUrl()).isEqualTo("profile-image.png");
    }

    @Test
    void 사용자_프로필_이미지_수정시_기본_이미지가_아닌_경우_이전_이미지_삭제() {
        // given
        String profileImageUrl = "profile-image-path";
        Member member = MemberFixture.memberWithNicknameAndProfileImage("바로", profileImageUrl);
        Member savedMember = memberRepository.save(member);
        UpdateProfileImageCommand command = new UpdateProfileImageCommand(
                savedMember.getId(),
                new MockMultipartFile("profile-image", "profile-image.png", "image/png", "profile-image".getBytes())
        );

        // when
        memberService.updateProfileImage(command);

        // then
        assertThat(savedMember.getProfileImageUrl()).isEqualTo("profile-image.png");
    }

    @Test
    void 사용자_탈퇴() {
        // given
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        DeleteMemberCommand command = new DeleteMemberCommand(savedMember.getId(), "사용 빈도가 낮아서");

        // when
        memberService.deleteMember(command);

        // then
        assertThat(memberRepository.findById(savedMember.getId())).isEmpty();
    }
}
