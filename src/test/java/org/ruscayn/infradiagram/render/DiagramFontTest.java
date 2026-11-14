package org.ruscayn.infradiagram.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiagramFontTest {

    private final DiagramFont font = new DiagramFont();

    @Test
    @DisplayName("함께 둔 글꼴은 한글과 영문과 숫자를 그릴 수 있다")
    void displaysKoreanAndLatin() {
        assertTrue(font.canDisplay("데이터 레이크 DATA LAKE 123"));
    }

    @Test
    @DisplayName("함께 둔 글꼴에 없는 그림 문자는 그릴 수 없다고 알린다")
    void reportsCharactersItCannotDisplay() {
        assertFalse(font.canDisplay("🚀"));
    }

    @Test
    @DisplayName("글자의 폭은 글자 크기에 비례한다")
    void widthGrowsWithFontSize() {
        double small = font.width("DATA LAKE", 0.8);
        double large = font.width("DATA LAKE", 80);

        assertEquals(large, small * 100, 1e-6);
        assertTrue(small > 0);
    }
}
