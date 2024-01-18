package com.baro.template.domain;

import com.baro.common.entity.BaseEntity;
import com.baro.member.domain.Member;
import com.baro.memofolder.domain.MemoFolder;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TemplateMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne
    @JoinColumn(name = "memo_folder_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemoFolder memoFolder;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Template template;

    private TemplateMember(Member member, MemoFolder memoFolder, Template template) {
        this.member = member;
        this.memoFolder = memoFolder;
        this.template = template;
    }

    private TemplateMember(Long id, Member member, MemoFolder memoFolder, Template template) {
        this.id = id;
        this.member = member;
        this.memoFolder = memoFolder;
        this.template = template;
    }

    public static TemplateMember of(Member member, MemoFolder memoFolder, Template template) {
        return new TemplateMember(member, memoFolder, template);
    }

    public static TemplateMember instanceForTest(Long id, Member member, MemoFolder memoFolder, Template template) {
        return new TemplateMember(id, member, memoFolder, template);
    }
}
