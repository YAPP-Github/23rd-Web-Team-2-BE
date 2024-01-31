package com.baro.archive.domain;

import com.baro.archive.exception.ArchiveException;
import com.baro.archive.exception.ArchiveExceptionType;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ArchiveTab {
    ALL("전체"),
    MEMO("끄적이는"),
    TEMPLATE("참고하는"),
    ;

    private final String name;

    public static ArchiveTab from(String tabName) {
        return Arrays.stream(values())
                .filter(tab -> tab.name().equalsIgnoreCase(tabName))
                .findFirst()
                .orElseThrow(() -> new ArchiveException(ArchiveExceptionType.NOT_EXIST_TAB));
    }
}
