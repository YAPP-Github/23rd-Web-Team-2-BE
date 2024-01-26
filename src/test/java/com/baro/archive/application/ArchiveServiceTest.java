package com.baro.archive.application;

import com.baro.archive.domain.ArchiveRepository;
import com.baro.archive.fake.FakeArchiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArchiveServiceTest {

    private ArchiveService archiveService;
    private ArchiveRepository archiveRepository;

    @BeforeEach
    void setUp() {
        archiveRepository = new FakeArchiveRepository();
        archiveService = new ArchiveService(archiveRepository);
    }
}
