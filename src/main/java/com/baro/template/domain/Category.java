package com.baro.template.domain;

import com.baro.template.exception.TemplateException;
import com.baro.template.exception.TemplateExceptionType;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Category {

    ASK,
    REPORT,
    CELEBRATE,
    THANK,
    COMFORT,
    REGARD,
    ETC,
    ;

    public static Category getCategory(String name) {
        return Arrays.stream(Category.values())
                .filter(category -> category.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new TemplateException(TemplateExceptionType.INVALID_CATEGORY));
    }
}
