package com.baro.template.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TemplateTest {

    @Test
    void 템플릿_저장시_저장횟수_증가() {
        // given
        var savedCount = 0;
        var template = Template.instanceForTest(1L, TemplateCategory.REPORT, "subCategory",
                "content", 0, savedCount);

        // when
        template.increaseSavedCount();

        // then
        assertThat(template.getSavedCount()).isEqualTo(savedCount + 1);
    }
}
