package com.baro.template.fixture;

import static com.baro.template.domain.Template.instanceForTest;

import com.baro.template.domain.Template;
import com.baro.template.domain.TemplateCategory;

public class TemplateFixture {

    public static Template 보고하기(int savedCount, int copiedCount) {
        return instanceForTest(TemplateCategory.REPORT, "상사", "content", savedCount, copiedCount);
    }

    public static Template 보고하기() {
        return 보고하기(0, 0);
    }

    public static Template 감사전하기(int savedCount, int copiedCount) {
        return instanceForTest(TemplateCategory.THANK, "후배", "content", savedCount, copiedCount);
    }

    public static Template 감사전하기() {
        return 감사전하기(0, 0);
    }
}
