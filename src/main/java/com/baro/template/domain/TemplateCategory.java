package com.baro.template.domain;

import com.baro.template.exception.TemplateException;
import com.baro.template.exception.TemplateExceptionType;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TemplateCategory {

    ASK("부탁하기"),
    REPORT("보고하기"),
    CELEBRATE("축하하기"),
    THANK("감사 전하기"),
    COMFORT("위로하기"),
    REGARD("안부 전하기"),
    ETC("기타"),
    ;

    private final String name;

    public static TemplateCategory getCategory(String name) {
        return Arrays.stream(TemplateCategory.values())
                .filter(category -> category.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new TemplateException(TemplateExceptionType.INVALID_CATEGORY));
    }
}
