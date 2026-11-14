package org.ruscayn.infradiagram.render;

import java.awt.Color;
import java.util.List;

/**
 * 그림에 쓰는 색이다. 무리마다 색 하나를 받고, 그 색을 옅은 단계와 진한 단계로 나눠 쓴다.
 *
 * 옅은 단계는 그 무리 모든 기둥의 윗면에 칠해서 무리를 구분한다.
 * 진한 단계는 글자가 붙은 기둥의 윗면과 글자 옆의 점에 칠해서 글자와 기둥을 짝짓는다.
 */
final class Palette {

    static final Color BACKGROUND = Color.WHITE;
    static final Color SURFACE = Color.WHITE;
    static final Color LINE = new Color(0x1E, 0x1E, 0x1E);
    static final Color LINK = new Color(0x9A, 0xA1, 0xA9);
    static final Color TEXT = new Color(0x15, 0x15, 0x15);

    private static final List<Color> LIGHT = List.of(
            new Color(0xDC, 0xEF, 0xFB), new Color(0xDD, 0xF3, 0xE6),
            new Color(0xFD, 0xEA, 0xD5), new Color(0xEA, 0xE2, 0xF8));
    private static final List<Color> STRONG = List.of(
            new Color(0x86, 0xC8, 0xF2), new Color(0x7F, 0xD1, 0xA6),
            new Color(0xF5, 0xB2, 0x6E), new Color(0xB3, 0x9B, 0xE6));

    Color light(int colorIndex) {
        return LIGHT.get(colorIndex % LIGHT.size());
    }

    Color strong(int colorIndex) {
        return STRONG.get(colorIndex % STRONG.size());
    }
}
