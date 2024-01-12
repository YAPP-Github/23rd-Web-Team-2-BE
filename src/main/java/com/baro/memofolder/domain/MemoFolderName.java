package com.baro.memofolder.domain;

import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MemoFolderName {

    private static final String DEFAULT_FOLDER_NAME = "기본";
    private static final int MAX_FOLDER_SIZE = 20;

    @Column(nullable = false)
    private String name;

    private MemoFolderName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (name.length() > MAX_FOLDER_SIZE) {
            throw new MemoFolderException(MemoFolderExceptionType.OVER_MAX_SIZE_NAME);
        }
    }

    public static MemoFolderName getDefault() {
        return new MemoFolderName(DEFAULT_FOLDER_NAME);
    }

    public static MemoFolderName from(String name) {
        return new MemoFolderName(name);
    }
}
