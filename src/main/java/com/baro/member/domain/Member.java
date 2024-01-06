package com.baro.member.domain;

import com.baro.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
/**
 * p4. 저희 회사 내 팀에서는 JPA에 name 외의 속성을 따로 주지 않고, DDL을 따로 관리하고 있어요.
 * 우선적으로 DB 권한 문제도 있긴 하지만,
 * JPA에서 생성해주는 DDL 이 의도한대로 나오지 않은 경우도 많아서
 * 직접 SQL을 작성하고, 파일이나 다른 wiki 등에 정리하는 편입니다.
 * 또한 hibernate.ddl-auto 을 사용하는 것도 같은 이유로 실무에서는 권장되진 않아요. (그런데 application.yml 파일이 안보이네요)
 *
 * 이건 저희 팀(YAPP)에서 ddl 관리 했던 방식 입니다.
 * https://github.com/YAPP-Github/onboard-server/tree/dev/adapter-out/rdb/src/main/resources/sql/ddl
 */
@Table(uniqueConstraints = @UniqueConstraint(
        name = "UK_oauth_id_oauth_service_type",
        columnNames = {"oAuthId", "oAuthServiceType"}
))
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
    public Member(String name, String email, String oAuthId, String oAuthServiceType) {
        this.name = name;
        this.email = email;
        this.oAuthId = oAuthId;
        this.oAuthServiceType = oAuthServiceType;
    }

    public Member(Long id, String name, String email, String oAuthId, String oAuthServiceType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.oAuthId = oAuthId;
        this.oAuthServiceType = oAuthServiceType;
    }
}
