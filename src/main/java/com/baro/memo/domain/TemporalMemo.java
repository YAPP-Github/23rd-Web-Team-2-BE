package com.baro.memo.domain;

import com.baro.common.entity.BaseEntity;
import com.baro.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TemporalMemo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Column(nullable = false)
    @Embedded
    private MemoContent content;

    @Column(length = 512)
    private String correctionContent;

    @OneToOne
    @JoinColumn(name = "memo_id", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Memo memo;

    public TemporalMemo(Long id, Member member, MemoContent content, String correctionContent, Memo memo) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.correctionContent = correctionContent;
        this.memo = memo;
    }

    public TemporalMemo(Member member, MemoContent content, String correctionContent, Memo memo) {
        this(null, member, content, correctionContent, memo);
    }

    public static TemporalMemo of(Member member, String content) {
        return new TemporalMemo(member, MemoContent.from(content), null, null);
    }
}
