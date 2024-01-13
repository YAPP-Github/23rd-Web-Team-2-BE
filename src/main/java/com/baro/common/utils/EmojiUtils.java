package com.baro.common.utils;

import com.vdurmont.emoji.EmojiParser;
import java.util.List;

public class EmojiUtils {

    public static int calculateLengthWithEmojis(String textWithEmoji) {
        List<String> emojis = EmojiParser.extractEmojis(textWithEmoji);
        String text = EmojiParser.removeAllEmojis(textWithEmoji);
        return text.length() + emojis.size();
    }
}
