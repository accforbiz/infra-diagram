package org.ruscayn.infradiagram.layout;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TopClearanceTest {

    private final TopClearance noGap = new TopClearance(0.5, 0);

    @Test
    @DisplayName("높이가 같으면 바닥이 딱 붙는 거리에서 윗면도 가려지지 않는다")
    void sameHeightNeedsOnlyFootprintDistance() {
        assertTrue(noGap.leavesTopVisible(pillar(0, 2.0, 1), pillar(0, 0, 1)));
        assertFalse(noGap.leavesTopVisible(pillar(0, 1.9, 1), pillar(0, 0, 1)));
    }

    @Test
    @DisplayName("앞 기둥이 화면에서 1 더 높으면 윗면이 2만큼 더 뒤로 가서, 뒤로 3 옆으로 1 떨어진 기둥의 윗면을 가린다")
    void tallerFrontPillarHidesTopBehindIt() {
        Standing front = pillar(1, 3, 2);
        Standing back = pillar(0, 0, 1);

        assertFalse(noGap.leavesTopVisible(front, back));
    }

    @Test
    @DisplayName("뒤 기둥의 윗면이 앞 기둥의 몸통과 나란한 깊이에 있으면 가로로 두 반지름만큼 떨어져야 보인다")
    void topLevelWithFrontBodyNeedsSidewaysDistance() {
        Standing back = pillar(0, 0, 1);

        assertTrue(noGap.leavesTopVisible(pillar(2.0, 3, 5), back));
        assertFalse(noGap.leavesTopVisible(pillar(1.9, 3, 5), back));
    }

    @Test
    @DisplayName("틈을 정해 두면 그 틈만큼 더 떨어져야 한다")
    void addsConfiguredGap() {
        TopClearance withGap = new TopClearance(0.5, 0.3);

        assertTrue(withGap.leavesTopVisible(pillar(0, 2.3, 1), pillar(0, 0, 1)));
        assertFalse(withGap.leavesTopVisible(pillar(0, 2.2, 1), pillar(0, 0, 1)));
    }

    private Standing pillar(double x, double y, double height) {
        return new Standing(new GroundPoint(x, y), 1, height);
    }
}
