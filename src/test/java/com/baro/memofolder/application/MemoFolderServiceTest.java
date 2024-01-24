package com.baro.memofolder.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fixture.MemberFixture;
import com.baro.memofolder.application.dto.GetMemoFolderResult;
import com.baro.memofolder.application.dto.RenameMemoFolderCommand;
import com.baro.memofolder.application.dto.SaveMemoFolderCommand;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderName;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
import com.baro.memofolder.fake.FakeMemoFolderRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemoFolderServiceTest {

    private MemoFolderService memoFolderService;
    private MemberRepository memberRepository;
    private MemoFolderRepository memoFolderRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        memoFolderRepository = new FakeMemoFolderRepository();
        memoFolderService = new MemoFolderService(memberRepository, memoFolderRepository);
    }

    @Test
    void 메모_폴더를_생성_한다() {
        // given
        Long memberId = memberRepository.save(MemberFixture.memberWithNickname("nickname1")).getId();
        String folderName = "testFolder";
        SaveMemoFolderCommand command = new SaveMemoFolderCommand(memberId, folderName);

        // when
        memoFolderService.saveMemoFolder(command);

        // then
        List<MemoFolder> all = memoFolderRepository.findAll();
        assertAll(
                () -> assertThat(all).hasSize(1),
                () -> assertThat(all.get(0).getName()).isEqualTo(MemoFolderName.from(folderName))
        );
    }


    @Test
    void 존재하지_않는_회원인_경우_예외_발생() {
        // given
        Long notExistMemberId = 999L;
        String folderName = "testFolder";
        SaveMemoFolderCommand command = new SaveMemoFolderCommand(notExistMemberId, folderName);

        // when & then
        assertThatThrownBy(() -> memoFolderService.saveMemoFolder(command))
                .isInstanceOf(MemberException.class)
                .extracting("exceptionType")
                .isEqualTo(MemberExceptionType.NOT_EXIST_MEMBER);
    }

    @Test
    void 폴더_이름이_중복된_경우_예외_발생() {
        // given
        Long memberId = memberRepository.save(MemberFixture.memberWithNickname("nickname1")).getId();
        String folderName = "testFolder";
        SaveMemoFolderCommand command = new SaveMemoFolderCommand(memberId, folderName);
        memoFolderService.saveMemoFolder(command);

        // when & then
        assertThatThrownBy(() -> memoFolderService.saveMemoFolder(command))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.NAME_DUPLICATION);
    }

    @Test
    void 회원의_모든_메모_폴더를_조회한다() {
        // given
        Member memberA = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        Member memberB = memberRepository.save(MemberFixture.memberWithNickname("nickname2"));
        memoFolderRepository.save(MemoFolder.defaultFolder(memberA));
        memoFolderRepository.save(MemoFolder.of(memberA, "폴더이름"));
        memoFolderRepository.save(MemoFolder.defaultFolder(memberB));

        // when
        List<GetMemoFolderResult> memoFolders = memoFolderService.getMemoFolder(memberA.getId());

        // then
        assertAll(
                () -> assertThat(memoFolders).hasSize(2),
                () -> assertThat(memoFolders).extracting("name")
                        .containsExactlyInAnyOrder("기본", "폴더이름")
        );
    }

    @Test
    void 폴더_이름을_변경한다() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        MemoFolder memoFolder = memoFolderRepository.save(MemoFolder.of(member, "변경전 폴더이름"));
        String newFolderName = "변경후 폴더이름";
        RenameMemoFolderCommand command = new RenameMemoFolderCommand(member.getId(), memoFolder.getId(),
                newFolderName);

        // when
        memoFolderService.renameMemoFolder(command);

        // then
        List<MemoFolder> all = memoFolderRepository.findAll();
        assertAll(
                () -> assertThat(all).hasSize(1),
                () -> assertThat(all.get(0).getName()).isEqualTo(MemoFolderName.from(newFolderName))
        );
    }

    @Test
    void 폴더이름_수정시_이름이_중복된_경우_예외_발생() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        String duplicatedFolderName = "중복 폴더이름";
        MemoFolder memoFolder = memoFolderRepository.save(MemoFolder.of(member, duplicatedFolderName));
        RenameMemoFolderCommand command = new RenameMemoFolderCommand(member.getId(), memoFolder.getId(),
                duplicatedFolderName);

        // when & then
        assertThatThrownBy(() -> memoFolderService.renameMemoFolder(command))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.NAME_DUPLICATION);
    }

    @Test
    void 폴더이름_수정시_제한길이가_초과된_경우_예외_발생() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        MemoFolder memoFolder = memoFolderRepository.save(MemoFolder.of(member, "변경전 폴더이름"));
        String newFolderName = "a".repeat(20);
        RenameMemoFolderCommand command = new RenameMemoFolderCommand(member.getId(), memoFolder.getId(),
                newFolderName);

        // when & then
        assertThatThrownBy(() -> memoFolderService.renameMemoFolder(command))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.OVER_MAX_SIZE_NAME);
    }

    @Test
    void 폴더이름_수정시_존재하지_않는_회원인_경우_예외_발생() {
        // given
        Long notExistMemberId = 999L;
        String folderName = "폴더이름";
        SaveMemoFolderCommand command = new SaveMemoFolderCommand(notExistMemberId, folderName);

        // when & then
        assertThatThrownBy(() -> memoFolderService.saveMemoFolder(command))
                .isInstanceOf(MemberException.class)
                .extracting("exceptionType")
                .isEqualTo(MemberExceptionType.NOT_EXIST_MEMBER);
    }

    @Test
    void 폴더이름_수정시_해당_폴더의_주인이_아닌_경우_예외_발생() {
        // given
        Member memberA = memberRepository.save(MemberFixture.memberWithNickname("바로1"));
        Member memberB = memberRepository.save(MemberFixture.memberWithNickname("바로2"));
        MemoFolder memoFolder = memoFolderRepository.save(MemoFolder.of(memberA, "변경전 폴더이름"));
        RenameMemoFolderCommand command = new RenameMemoFolderCommand(memberB.getId(), memoFolder.getId(),
                "변경후 폴더이름");

        // when & then
        assertThatThrownBy(() -> memoFolderService.renameMemoFolder(command))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 폴더이름_수정시_빈_문자열일_경우_예외_발생() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        MemoFolder memoFolder = memoFolderRepository.save(MemoFolder.of(member, "변경전 폴더이름"));
        String newFolderName = "";
        RenameMemoFolderCommand command = new RenameMemoFolderCommand(member.getId(), memoFolder.getId(),
                newFolderName);

        // when & then
        assertThatThrownBy(() -> memoFolderService.renameMemoFolder(command))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.EMPTY_NAME);
    }
}
