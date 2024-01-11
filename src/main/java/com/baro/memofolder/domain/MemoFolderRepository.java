package com.baro.memofolder.domain;

import java.util.List;

public interface MemoFolderRepository {

    MemoFolder save(MemoFolder memoFolder);

    List<MemoFolder> findAll();
}
