package com.baro.member.domain;

import com.baro.common.entity.BaseEntity;
import jakarta.persistence.*;
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

    private String nickname;

    private String profileImageUrl;

    @Column(nullable = false)
    private String oAuthId;

    @Column(nullable = false)
    private String oAuthServiceType;

    @Builder
    public Member(String name, String email, String nickname, String oAuthId, String oAuthServiceType) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.oAuthId = oAuthId;
        this.oAuthServiceType = oAuthServiceType;
    }

    public Member(Long id, String name, String email, String nickname, String oAuthId, String oAuthServiceType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.oAuthId = oAuthId;
        this.oAuthServiceType = oAuthServiceType;
    }
}
