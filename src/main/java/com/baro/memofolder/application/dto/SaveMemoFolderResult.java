package com.baro.memofolder.application.dto;

import com.baro.memofolder.domain.MemoFolder;

public record SaveMemoFolderResult(
        Long id
) {

    public static SaveMemoFolderResult from(MemoFolder memoFolder) {
        return new SaveMemoFolderResult(memoFolder.getId());
    }
}
