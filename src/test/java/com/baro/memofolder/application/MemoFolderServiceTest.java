package com.baro.memofolder.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fixture.MemberFixture;
import com.baro.memofolder.application.dto.SaveMemoFolderCommand;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
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
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName()).isEqualTo(folderName);
    }


    @Test
    void 존재하지_않는_회원인_경우_예외_발생() {
        // given
        Long notExistMemberId = 999L;
        String folderName = "testFolder";
        SaveMemoFolderCommand command = new SaveMemoFolderCommand(notExistMemberId, folderName);

        // when & then
        assertThatThrownBy(() -> memoFolderService.saveMemoFolder(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 회원입니다.");
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 폴더명입니다.");
    }
}
