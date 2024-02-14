package com.baro.member.domain;

import com.baro.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberWithdrawalInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(nullable = false)
    private String reason;

    public MemberWithdrawalInfo(Long id, String reason) {
        this.id = id;
        this.reason = reason;
    }

    public MemberWithdrawalInfo(String reason) {
        this(null, reason);
    }
}
