package com.baro.memofolder.application.dto;

import com.baro.memofolder.domain.MemoFolder;

public record GetMemoFolderResult(
        Long id,
        String name
) {

    public static GetMemoFolderResult from(MemoFolder memoFolder) {
        return new GetMemoFolderResult(
                memoFolder.getId(),
                memoFolder.getName().value()
        );
    }
}
