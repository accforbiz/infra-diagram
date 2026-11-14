package org.ruscayn.infradiagram.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 무리 안의 자리를 가운데부터 바깥으로 고리처럼 한 겹씩 만든다.
 *
 * 자리는 벌집 모양의 격자 위에 있다. 이웃한 자리 사이의 거리는 모두 1이다.
 * 가운데에 1자리, 첫 고리에 6자리, 둘째 고리에 12자리가 있다(n번째 고리에는 6 × n자리).
 * 격자의 줄은 가로로 놓이고, 이웃한 줄은 반 칸씩 어긋나며 앞뒤로 ROW_DEPTH만큼 떨어진다.
 * 반 칸씩 어긋나므로 어느 자리도 이웃 줄의 자리 바로 앞이나 바로 뒤에 놓이지 않는다.
 *
 * 격자 좌표 (q, r)은 바닥의 점 (q + r / 2, r × ROW_DEPTH)가 된다.
 * 예: (1, 0)은 가운데의 오른쪽 (1, 0), (0, 1)은 오른쪽 앞 (0.5, 0.866)이다.
 */
final class RingSlots {

    static final double ROW_DEPTH = Math.sqrt(3) / 2;

    private static final int SIDES = 6;
    private static final int[][] CORNERS = {{1, 0}, {0, 1}, {-1, 1}, {-1, 0}, {0, -1}, {1, -1}};
    private static final int[][] STEPS = {{-1, 1}, {-1, 0}, {0, -1}, {1, -1}, {1, 0}, {0, 1}};

    List<GroundPoint> positions(int count) {
        List<GroundPoint> positions = new ArrayList<>(List.of(new GroundPoint(0, 0)));
        for (int ring = 1; positions.size() < count; ring++) {
            positions.addAll(spread(ring(ring), count - positions.size()));
        }
        return List.copyOf(positions);
    }

    private List<GroundPoint> ring(int ring) {
        return IntStream.range(0, SIDES * ring).mapToObj(slot -> slot(ring, slot)).toList();
    }

    /**
     * 고리의 slot번째 자리를 구한다. 고리는 육각형이고 한 변에 ring개의 자리가 있다.
     *
     * 오른쪽 꼭짓점에서 출발해 앞쪽을 지나 한 바퀴 돈다.
     * 예: 둘째 고리(ring = 2)의 3번 자리는 1번 변(3 / 2)의 둘째 자리(3 % 2 = 1)다.
     * 1번 변의 꼭짓점 (0, 2)에서 한 걸음 (-1, 0)을 가서 격자 좌표 (-1, 2)가 된다.
     */
    private GroundPoint slot(int ring, int slot) {
        int side = slot / ring;
        int along = slot % ring;
        int q = ring * CORNERS[side][0] + along * STEPS[side][0];
        int r = ring * CORNERS[side][1] + along * STEPS[side][1];
        return new GroundPoint(q + r / 2.0, r * ROW_DEPTH);
    }

    /**
     * 고리를 다 채우지 못할 때 한쪽에 몰리지 않도록 고르게 띄워 고른다.
     *
     * 예: 6자리 고리에 3개만 놓으면 0, 2, 4번 자리를 써서 세 방향으로 고르게 놓인다.
     * 0, 1, 2번 자리를 쓰면 무리가 한쪽으로 치우쳐 보인다.
     *
     * 둘째 고리부터는 꼭짓점이 아니라 변의 가운데 쪽 자리에서 시작한다.
     * 꼭짓점 자리는 가운데에서 가장 멀어서, 하나만 놓으면 무리에서 동떨어져 보이기 때문이다.
     * 예: 12자리인 둘째 고리에 하나만 놓으면 0번(꼭짓점)이 아니라 1번 자리에 놓인다.
     */
    private List<GroundPoint> spread(List<GroundPoint> slots, int needed) {
        if (needed >= slots.size()) {
            return slots;
        }
        int size = slots.size();
        int start = size / SIDES / 2;
        return IntStream.range(0, needed).mapToObj(index -> slots.get((start + index * size / needed) % size)).toList();
    }
}
