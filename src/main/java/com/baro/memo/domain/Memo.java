package com.baro.memo.domain;

import com.baro.common.entity.BaseEntity;
import com.baro.member.domain.Member;
import com.baro.memofolder.domain.MemoFolder;
import jakarta.persistence.AttributeOverride;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Memo extends BaseEntity {

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

    @Embedded
    @AttributeOverride(name = "content", column = @Column(name = "content", nullable = false))
    private MemoContent content;

    private Memo(Member member, MemoFolder memoFolder, MemoContent content) {
        this.member = member;
        this.memoFolder = memoFolder;
        this.content = content;
    }

    public static Memo of(Member member, MemoFolder memoFolder, MemoContent content) {
        return new Memo(member, memoFolder, content);
    }
}
