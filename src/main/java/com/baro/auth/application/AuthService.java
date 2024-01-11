package com.baro.auth.application;

import com.baro.auth.application.dto.SignInDto;
import com.baro.auth.domain.Token;
import com.baro.member.application.MemberCreator;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberCreator memberCreator;
    private final TokenTranslator tokenTranslator;
    private final MemoFolderRepository memoFolderRepository;

    public Token signIn(SignInDto dto) {
        Member member = memberRepository.findByOAuthIdAndOAuthServiceType(dto.oauthId(), dto.oauthType())
                .orElseGet(saveNemMember(dto));

        Token token = tokenTranslator.encode(member.getId());
        // TODO refresh token 저장
        return token;
    }

    private Supplier<Member> saveNemMember(final SignInDto dto) {
        return () -> {
            Member member = memberCreator.create(dto.name(), dto.email(), dto.oauthId(), dto.oauthType());
            memoFolderRepository.save(MemoFolder.defaultFolder(member));
            return memberRepository.save(member);
        };
    }
}
