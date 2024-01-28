package com.baro.member.domain;

import com.baro.common.entity.BaseEntity;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_oauth_id_oauth_service_type", columnNames = {"oAuthId", "oAuthServiceType"}),
        @UniqueConstraint(name = "UK_nickname", columnNames = {"nickname"})
})
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Embedded
    private MemberNickname nickname;

    private String profileImageUrl;

    @Column(nullable = false)
    private String oAuthId;

    @Column(nullable = false)
    private String oAuthServiceType;

    @Builder
    public Member(String name, String email, String nickname, String oAuthId, String oAuthServiceType,
                  String profileImageUrl) {
        this.name = name;
        this.email = email;
        this.nickname = MemberNickname.from(nickname);
        this.oAuthId = oAuthId;
        this.oAuthServiceType = oAuthServiceType;
        this.profileImageUrl = profileImageUrl;
    }

    public Member(Long id, String name, String email, String nickname, String oAuthId, String oAuthServiceType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = MemberNickname.from(nickname);
        this.oAuthId = oAuthId;
        this.oAuthServiceType = oAuthServiceType;
    }

    public Member(Long id, String name, String email, String nickname, String profileImageUrl, String oAuthId,
                  String oAuthServiceType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = MemberNickname.from(nickname);
        this.profileImageUrl = profileImageUrl;
        this.oAuthId = oAuthId;
        this.oAuthServiceType = oAuthServiceType;
    }

    public void updateProfile(String name, String nickname) {
        this.name = name;
        this.nickname = MemberNickname.from(nickname);
    }

    public void deleteProfileImage() {
        if (Objects.isNull(this.profileImageUrl)) {
            throw new MemberException(MemberExceptionType.NOT_EXIST_PROFILE_IMAGE);
        }
        this.profileImageUrl = null;
    }
}
