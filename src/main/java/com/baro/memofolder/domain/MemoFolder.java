package com.baro.memofolder.domain;

import com.baro.common.entity.BaseEntity;
import com.baro.member.domain.Member;
import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
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
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemoFolder extends BaseEntity {

    private static final String DEFAULT_FOLDER_NAME = "기본";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Embedded
    private MemoFolderName name;

    private MemoFolder(Member member, MemoFolderName name) {
        this.member = member;
        this.name = name;
    }

    public static MemoFolder defaultFolder(Member member) {
        return new MemoFolder(member, MemoFolderName.getDefault());
    }

    public static MemoFolder of(Member member, String name) {
        return new MemoFolder(member, MemoFolderName.from(name));
    }

    public void matchOwner(Long memberId) {
        if (!Objects.equals(this.member.getId(), memberId)) {
            throw new MemoFolderException(MemoFolderExceptionType.NOT_MATCH_OWNER);
        }
    }

    public void rename(String name) {
        this.name = MemoFolderName.from(name);
    }
}
