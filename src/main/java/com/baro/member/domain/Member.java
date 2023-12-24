package com.baro.member.domain;

import com.baro.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(
        name = "UK_oauth_id_oauth_service_type",
        columnNames = {"oAuthId", "oAuthServiceType"}
))
@Entity
public class Member extends BaseEntity {

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
    public Member(String name, String email, String oAuthId, String oAuthServiceType) {
        this.name = name;
        this.email = email;
        this.oAuthId = oAuthId;
        this.oAuthServiceType = oAuthServiceType;
    }
}
