package org.ruscayn.infradiagram.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClusterStyleTest {

    private static final int COLORS = 4;

    @Test
    @DisplayName("색이 네 가지이면 무리 열두 개까지 모양과 색의 짝이 겹치지 않는다")
    void givesEveryClusterItsOwnPair() {
        Set<ClusterStyle> styles = IntStream.range(0, 12)
                .mapToObj(index -> ClusterStyle.forCluster(index, COLORS))
                .collect(Collectors.toSet());

        assertEquals(12, styles.size());
    }

    @Test
    @DisplayName("넷째 무리는 첫째 무리와 모양이 다시 같아지지만 색이 다르다")
    void fourthClusterRepeatsShapeWithAnotherColor() {
        ClusterStyle first = ClusterStyle.forCluster(0, COLORS);
        ClusterStyle fourth = ClusterStyle.forCluster(3, COLORS);

        assertEquals(first.shape(), fourth.shape());
        assertNotEquals(first.colorIndex(), fourth.colorIndex());
    }

    @Test
    @DisplayName("모양은 삼각기둥, 육각기둥, 원기둥의 순서로 배정한다")
    void assignsShapesInOrder() {
        assertEquals(PillarShape.TRIANGULAR_PRISM, ClusterStyle.forCluster(0, COLORS).shape());
        assertEquals(PillarShape.HEXAGONAL_PRISM, ClusterStyle.forCluster(1, COLORS).shape());
        assertEquals(PillarShape.CYLINDER, ClusterStyle.forCluster(2, COLORS).shape());
    }
}
