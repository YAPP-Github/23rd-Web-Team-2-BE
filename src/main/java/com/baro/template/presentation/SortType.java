package com.baro.template.presentation;

import com.baro.template.domain.Template;
import com.baro.template.exception.SortException;
import com.baro.template.exception.SortExceptionType;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
@Getter
public enum SortType {

    NEW(Sort.TypedSort.sort(Template.class).by(Template::getCreatedAt).descending()),
    COPY(Sort.TypedSort.sort(Template.class).by(Template::getCopiedCount).descending()),
    SAVE(Sort.TypedSort.sort(Template.class).by(Template::getSavedCount).descending());

    private final Sort sort;

    public static Sort getSort(String sortName) {
        return Arrays.stream(SortType.values())
                .filter(sortType -> sortType.name().equalsIgnoreCase(sortName))
                .findFirst()
                .orElseThrow(() -> new SortException(SortExceptionType.INVALID_SORT_TYPE))
                .getSort();
    }
}
