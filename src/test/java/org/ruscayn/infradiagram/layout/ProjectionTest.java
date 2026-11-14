package org.ruscayn.infradiagram.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ruscayn.infradiagram.scene.ScreenPoint;

class ProjectionTest {

    private static final double TOLERANCE = 1e-9;

    private final Projection projection = Projection.lookingDownAt(30);

    @Test
    @DisplayName("30도로 내려다보면 바닥의 점 (3, 4)는 앞뒤 방향만 절반으로 눌려 화면의 (3, 2)가 된다")
    void flattensDepthByHalfAtThirtyDegrees() {
        ScreenPoint point = projection.onGround(new GroundPoint(3, 4));

        assertEquals(3, point.x(), TOLERANCE);
        assertEquals(2, point.y(), TOLERANCE);
    }

    @Test
    @DisplayName("30도로 내려다보면 높이 2는 화면에서 2 × cos 30도 = 약 1.732가 된다")
    void shortensHeightAtThirtyDegrees() {
        assertEquals(1.7320508, projection.screenHeight(2), 1e-6);
    }
}
