package com.baro.memo.domain;

import com.baro.archive.domain.Archive;
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

    @Column(columnDefinition = "TEXT")
    private String styledCorrectionContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Archive archive;

    public TemporalMemo(Long id, Member member, MemoContent content, MemoContent correctionContent, Archive archive) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.correctionContent = correctionContent;
        this.archive = archive;
    }

    public TemporalMemo(Member member, MemoContent content, MemoContent correctionContent, Archive archive) {
        this(null, member, content, correctionContent, archive);
    }

    public static TemporalMemo of(Member member, String content) {
        return new TemporalMemo(member, MemoContent.from(content), null, null);
    }

    public void updateContent(MemoContent memoContent) {
        this.content = memoContent;
    }

    public void matchOwner(Long memberId) {
        if (!Objects.equals(this.member.getId(), memberId)) {
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

    public void archived(Archive archive) {
        this.archive = archive;
    }

    public void applyCorrection(MemoContent correctionContent, String styledCorrectionContent) {
        if (isCorrected()) {
            throw new TemporalMemoException(TemporalMemoExceptionType.ALREADY_CORRECTED);
        }
        this.correctionContent = correctionContent;
        this.styledCorrectionContent = styledCorrectionContent;
    }

    public boolean isArchived() {
        return Objects.nonNull(this.archive);
    }
}
