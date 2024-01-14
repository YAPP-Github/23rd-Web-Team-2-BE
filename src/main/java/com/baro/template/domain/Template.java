package com.baro.template.domain;

import com.baro.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Template extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    private String subCategory;

    @Column(length = 512, nullable = false)
    private String content;

    private int copiedCount;

    private int savedCount;

    public Template(Category category, String subCategory, String content, int copiedCount, int savedCount) {
        this.category = category;
        this.subCategory = subCategory;
        this.content = content;
        this.copiedCount = copiedCount;
        this.savedCount = savedCount;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Template(Category category, String subCategory, String content) {
        this.category = category;
        this.subCategory = subCategory;
        this.content = content;
        this.copiedCount = 0;
        this.savedCount = 0;
    }
}
