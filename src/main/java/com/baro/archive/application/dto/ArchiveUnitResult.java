package com.baro.archive.application.dto;

import com.baro.archive.domain.Archive;
import com.baro.archive.domain.ArchiveTab;

public record ArchiveUnitResult(
        Long archiveId,
        String tabName,
        String categoryName,
        String content,
        Integer copiedCount,
        Integer savedCount
) {

    public static ArchiveUnitResult of(Archive archive) {
        if (archive.getTemplate() == null) {
            return memoToArchiveUnit(archive);
        }
        return templateToArchiveUnit(archive);
    }

    private static ArchiveUnitResult memoToArchiveUnit(Archive archive) {
        return new ArchiveUnitResult(
                archive.getId(),
                ArchiveTab.MEMO.getName(),
                null,
                archive.getContent().value(),
                null,
                null
        );
    }

    private static ArchiveUnitResult templateToArchiveUnit(Archive archive) {
        return new ArchiveUnitResult(
                archive.getId(),
                ArchiveTab.TEMPLATE.getName(),
                archive.getTemplate().getCategory().getName(),
                archive.getContent().value(),
                archive.getTemplate().getCopiedCount(),
                archive.getTemplate().getSavedCount()
        );
    }
}
