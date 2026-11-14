package org.ruscayn.infradiagram.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.scene.Placement;
import org.ruscayn.infradiagram.settings.CanvasSettings;

class CanvasFitTest {

    private final CanvasFit fit = new CanvasFit(new CanvasSettings(1920, 1080, 60, 60));

    @Test
    @DisplayName("가로 100, 세로 20인 내용은 가로가 먼저 차므로 배율 18로 줄여 가운데에 놓는다")
    void shrinksToFitWidth() {
        assertEquals(new Placement(18, 60, 360), fit.fit(new Box(0, 0, 100, 20)));
    }

    @Test
    @DisplayName("내용이 작아도 배율의 상한 60을 넘겨 키우지 않는다")
    void doesNotEnlargeBeyondMaxScale() {
        assertEquals(new Placement(60, 660, 240), fit.fit(new Box(0, 0, 10, 10)));
    }

    @Test
    @DisplayName("내용이 원점에서 떨어져 있어도 그림의 가운데로 옮긴다")
    void movesOffsetContentToCentre() {
        assertEquals(new Placement(60, 360, -60), fit.fit(new Box(5, 5, 10, 10)));
    }
}
