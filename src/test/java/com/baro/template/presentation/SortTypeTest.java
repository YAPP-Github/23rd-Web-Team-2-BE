package com.baro.template.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.template.domain.Template;
import com.baro.template.exception.SortException;
import com.baro.template.exception.SortExceptionType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SortTypeTest {

    @Test
    void 해당하는_정렬_타입을_반환한다() {
        // given
        var sortType = "new";

        // when
        var result = SortType.getSort(sortType);

        // then
        assertThat(result).isEqualTo(Sort.TypedSort.sort(Template.class).by(Template::getCreatedAt).descending());
    }

    @Test
    void 해당하는_정렬_타입이_없을_경우_예외를_반환한다() {
        // given
        var sortType = "nothing";

        // when & then
        assertThatThrownBy(() -> SortType.getSort(sortType))
                .isInstanceOf(SortException.class)
                .extracting("exceptionType")
                .isEqualTo(SortExceptionType.INVALID_SORT_TYPE);
    }
}
