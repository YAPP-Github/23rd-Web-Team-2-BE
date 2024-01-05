package com.baro.member.fake;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeMemberRepository implements MemberRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final Map<Long, Member> members = new ConcurrentHashMap<>();

    @Override
    public Optional<Member> findByOAuthIdAndOAuthServiceType(String oAuthId, String oAuthServiceType) {
        return members.values().stream()
                .filter(member -> member.getOAuthId().equals(oAuthId)
                        && member.getOAuthServiceType().equals(oAuthServiceType))
                .findAny();
    }

    @Override
    public Member save(Member member) {
        if (Objects.isNull(member.getId())) {
            Member newMember = new Member(
                    id.getAndIncrement(),
                    member.getName(),
                    member.getEmail(),
                    member.getNickname(),
                    member.getOAuthId(),
                    member.getOAuthServiceType()
            );
            members.put(newMember.getId(), newMember);
            return newMember;
        }
        members.put(member.getId(), member);
        return member;
    }

    @Override
    public List<Member> findAll() {
        return List.copyOf(members.values());
    }

    @Override
    public boolean existByNickname(String nickname) {
        return members.values().stream()
                .anyMatch(member -> member.getNickname().equals(nickname));
    }
}
