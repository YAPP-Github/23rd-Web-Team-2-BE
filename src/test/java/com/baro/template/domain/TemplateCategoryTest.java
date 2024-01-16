package com.baro.template.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.template.exception.TemplateException;
import com.baro.template.exception.TemplateExceptionType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TemplateCategoryTest {

    @Test
    void 해당하는_카테고리를_반환한다() {
        // given
        var category = "report";

        // when
        var result = TemplateCategory.getCategory(category);

        // then
        assertThat(result).isEqualTo(TemplateCategory.REPORT);
    }

    @Test
    void 해당하는_카테고리가_없을_경우_예외를_반환한다() {
        // given
        var category = "nothing";

        // when & then
        assertThatThrownBy(() -> TemplateCategory.getCategory(category))
                .isInstanceOf(TemplateException.class)
                .extracting("exceptionType")
                .isEqualTo(TemplateExceptionType.INVALID_CATEGORY);
    }
}
