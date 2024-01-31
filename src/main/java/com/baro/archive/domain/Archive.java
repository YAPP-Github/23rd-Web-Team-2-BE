package com.baro.archive.domain;

import com.baro.common.entity.BaseEntity;
import com.baro.member.domain.Member;
import com.baro.memo.domain.MemoContent;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.template.domain.Template;
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
public class Archive extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_folder_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemoFolder memoFolder;

    @Embedded
    @AttributeOverride(name = "content", column = @Column(name = "content", nullable = false))
    private MemoContent content;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Template template;

    public Archive(Long id, Member member, MemoFolder memoFolder, MemoContent content, Template template) {
        this.id = id;
        this.member = member;
        this.memoFolder = memoFolder;
        this.content = content;
        this.template = template;
    }

    public Archive(Long id, Member member, MemoFolder memoFolder, MemoContent memoContent) {
        this(id, member, memoFolder, memoContent, null);
    }

    public Archive(Member member, MemoFolder memoFolder, MemoContent content, Template template) {
        this(null, member, memoFolder, content, template);
    }

    public Archive(Member member, MemoFolder memoFolder, MemoContent content) {
        this(null, member, memoFolder, content, null);
    }

    public boolean isMemo() {
        return Objects.isNull(this.template);
    }

    public void changeMemoFolder(MemoFolder memoFolder) {
        this.memoFolder = memoFolder;
    }
}
