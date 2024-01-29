package com.baro.archive.fixture;

import com.baro.archive.domain.Archive;
import com.baro.member.domain.Member;
import com.baro.memo.domain.MemoContent;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.template.domain.Template;
import java.util.concurrent.atomic.AtomicLong;

public class ArchiveFixture {

    private static final AtomicLong id = new AtomicLong(1);

    public static Archive 끄적이는_아카이브1(Member member, MemoFolder memoFolder) {
        return new Archive(id.getAndIncrement(), member, memoFolder, MemoContent.from("끄적이는 아카이브1"));
    }

    public static Archive 끄적이는_아카이브2(Member member, MemoFolder memoFolder) {
        return new Archive(id.getAndIncrement(), member, memoFolder, MemoContent.from("끄적이는 아카이브2"));
    }

    public static Archive 참고하는_아카이브1(Member member, MemoFolder memoFolder, Template template) {
        return new Archive(id.getAndIncrement(), member, memoFolder, MemoContent.from("끄적이는 아카이브1"), template);
    }

    public static Archive 참고하는_아카이브2(Member member, MemoFolder memoFolder, Template template) {
        return new Archive(id.getAndIncrement(), member, memoFolder, MemoContent.from("끄적이는 아카이브2"), template);
    }
}
