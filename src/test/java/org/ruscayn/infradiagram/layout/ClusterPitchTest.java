package org.ruscayn.infradiagram.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClusterPitchTest {

    private static final double TOLERANCE = 1e-6;

    @Test
    @DisplayName("크기가 같은 기둥만 있으면 바닥이 틈만큼 떨어지는 거리, 곧 2 + 0.3 = 2.3이 된다")
    void sameSizeNeedsOnlyFootprintGap() {
        ClusterPitch pitch = new ClusterPitch(new TopClearance(0.5, 0.3), 0.3);
        List<Standing> pillars = new RingSlots().positions(7).stream().map(spot -> new Standing(spot, 1, 2)).toList();

        assertEquals(2.3, pitch.of(pillars), TOLERANCE);
    }

    @Test
    @DisplayName("높은 기둥 바로 뒤에 낮은 기둥이 있으면 윗면이 보일 때까지 벌린다")
    void widensUntilTopBehindTallPillarIsVisible() {
        ClusterPitch pitch = new ClusterPitch(new TopClearance(0.5, 0), 0);
        Standing tallInFront = new Standing(new GroundPoint(0, 1), 1, 3);
        Standing shortBehind = new Standing(new GroundPoint(0, 0), 1, 1);

        // 높이 차이 2는 평면에서 2 / 0.5 = 4이고, 여기에 두 반지름의 합 2를 더한 6만큼 떨어져야 한다.
        assertEquals(6.0, pitch.of(List.of(tallInFront, shortBehind)), TOLERANCE);
    }

    @Test
    @DisplayName("낮은 기둥이 앞에 있으면 윗면을 가리지 않으므로 바닥이 붙는 거리 2면 된다")
    void shortPillarInFrontNeedsOnlyFootprintDistance() {
        ClusterPitch pitch = new ClusterPitch(new TopClearance(0.5, 0), 0);
        Standing shortInFront = new Standing(new GroundPoint(0, 1), 1, 1);
        Standing tallBehind = new Standing(new GroundPoint(0, 0), 1, 3);

        assertEquals(2.0, pitch.of(List.of(shortInFront, tallBehind)), TOLERANCE);
    }
}
