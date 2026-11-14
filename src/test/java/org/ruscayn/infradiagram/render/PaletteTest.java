package org.ruscayn.infradiagram.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ruscayn.infradiagram.scene.ClusterColor;
import org.ruscayn.infradiagram.settings.DrawingSettings;

class PaletteTest {

    private final Palette palette = new Palette();

    @Test
    @DisplayName("색 번호마다, 그리고 회색에 서로 다른 색이 정해져 있다")
    void givesEveryColorNumberAndGrayADifferentColor() {
        Set<Color> colors = new HashSet<>();
        colors.add(palette.of(ClusterColor.neutral()));
        for (int number = 0; number < palette.clusterColorCount(); number++) {
            colors.add(palette.of(ClusterColor.numbered(number)));
        }

        assertEquals(palette.clusterColorCount() + 1, colors.size());
    }

    @Test
    @DisplayName("입력이 허용하는 만큼의 무리에 줄 색이 모두 있다")
    void hasAColorForEveryClusterTheInputAllows() {
        int allowed = DrawingSettings.standard().limits().maxMultiElementClusters();

        assertTrue(palette.clusterColorCount() >= allowed);
    }
}
