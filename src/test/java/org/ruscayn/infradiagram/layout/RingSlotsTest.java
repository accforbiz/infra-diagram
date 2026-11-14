package org.ruscayn.infradiagram.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RingSlotsTest {

    private static final double ROW = Math.sqrt(3) / 2;
    private static final double TOLERANCE = 1e-9;

    private final RingSlots slots = new RingSlots();

    @Test
    @DisplayName("하나만 놓으면 가운데 자리에 놓인다")
    void placesSingleElementAtCentre() {
        assertSamePoints(List.of(new GroundPoint(0, 0)), slots.positions(1));
    }

    @Test
    @DisplayName("일곱 개를 놓으면 가운데 자리와 그 둘레 여섯 방향의 자리를 손으로 구한 좌표대로 쓴다")
    void fillsCentreAndFirstRing() {
        List<GroundPoint> expected = List.of(new GroundPoint(0, 0), new GroundPoint(1, 0),
                new GroundPoint(0.5, ROW), new GroundPoint(-0.5, ROW), new GroundPoint(-1, 0),
                new GroundPoint(-0.5, -ROW), new GroundPoint(0.5, -ROW));

        assertSamePoints(expected, slots.positions(7));
    }

    @Test
    @DisplayName("고리를 다 채우지 못하면 한쪽에 몰지 않고 고르게 띄운다")
    void spreadsPartialRingEvenly() {
        List<GroundPoint> expected = List.of(new GroundPoint(0, 0), new GroundPoint(1, 0),
                new GroundPoint(-0.5, ROW), new GroundPoint(-0.5, -ROW));

        assertSamePoints(expected, slots.positions(4));
    }

    @Test
    @DisplayName("여덟째는 둘째 고리로 넘어가고, 꼭짓점이 아니라 변의 가운데 쪽 자리에 놓인다")
    void startsSecondRingAwayFromCorner() {
        GroundPoint eighth = slots.positions(8).get(7);

        assertSamePoints(List.of(new GroundPoint(1.5, ROW)), List.of(eighth));
    }

    @Test
    @DisplayName("둘째 고리까지 다 채운 열아홉 자리는 서로 1 이상 떨어져 있다")
    void keepsEverySlotOneApart() {
        List<GroundPoint> positions = slots.positions(19);

        assertEquals(19, positions.size());
        assertTrue(closestDistance(positions) >= 1 - TOLERANCE);
    }

    private double closestDistance(List<GroundPoint> points) {
        double closest = Double.POSITIVE_INFINITY;
        for (int first = 0; first < points.size(); first++) {
            for (int second = first + 1; second < points.size(); second++) {
                closest = Math.min(closest, distance(points.get(first), points.get(second)));
            }
        }
        return closest;
    }

    private double distance(GroundPoint first, GroundPoint second) {
        return Math.hypot(first.x() - second.x(), first.y() - second.y());
    }

    private void assertSamePoints(List<GroundPoint> expected, List<GroundPoint> actual) {
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertEquals(expected.get(index).x(), actual.get(index).x(), TOLERANCE, "x of point " + index);
            assertEquals(expected.get(index).y(), actual.get(index).y(), TOLERANCE, "y of point " + index);
        }
    }
}
