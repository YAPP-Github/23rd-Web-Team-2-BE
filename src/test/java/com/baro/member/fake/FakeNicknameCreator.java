package com.baro.member.fake;

import com.baro.member.application.NicknameCreator;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FakeNicknameCreator implements NicknameCreator {

    private Queue<String> nicknames;

    public FakeNicknameCreator(List<String> nicknames) {
        this.nicknames = new LinkedList<>(nicknames);
    }

    @Override
    public String create() {
        return nicknames.poll();
    }
}
