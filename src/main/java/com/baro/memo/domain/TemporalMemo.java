package com.baro.memo.domain;

import com.baro.common.entity.BaseEntity;
import com.baro.member.domain.Member;
import com.baro.memo.exception.TemporalMemoException;
import com.baro.memo.exception.TemporalMemoExceptionType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Embedded
    @AttributeOverride(name = "content", column = @Column(name = "content", nullable = false))
    private MemoContent content;

    @Embedded
    @AttributeOverride(name = "content", column = @Column(name = "correction_content"))
    private MemoContent correctionContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Memo memo;

    public TemporalMemo(Long id, Member member, MemoContent content, MemoContent correctionContent, Memo memo) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.correctionContent = correctionContent;
        this.memo = memo;
    }

    public TemporalMemo(Member member, MemoContent content, MemoContent correctionContent, Memo memo) {
        this(null, member, content, correctionContent, memo);
    }

    public static TemporalMemo of(Member member, String content) {
        return new TemporalMemo(member, MemoContent.from(content), null, null);
    }

    public void updateContent(MemoContent memoContent) {
        this.content = memoContent;
    }

    public void matchOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new TemporalMemoException(TemporalMemoExceptionType.NOT_MATCH_OWNER);
        }
    }

    public boolean isCorrected() {
        return Objects.nonNull(this.correctionContent);
    }

    public MemoContent getArchivingContent() {
        if (isCorrected()) {
            return this.correctionContent;
        }
        return this.content;
    }

    public void archivedAsMemo(Memo memo) {
        this.memo = memo;
    }
}
