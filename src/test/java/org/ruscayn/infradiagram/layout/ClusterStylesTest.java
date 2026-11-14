package org.ruscayn.infradiagram.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ruscayn.infradiagram.model.Cluster;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.model.Scale;
import org.ruscayn.infradiagram.scene.ClusterColor;
import org.ruscayn.infradiagram.scene.ClusterStyle;
import org.ruscayn.infradiagram.scene.PillarShape;

class ClusterStylesTest {

    private static final ClusterColor GRAY = ClusterColor.neutral();

    private final ClusterStyles styles = new ClusterStyles();

    @Test
    @DisplayName("요소가 둘 이상인 무리는 저마다 다른 색을 0번부터 차례로 받는다")
    void givesEveryMultiElementClusterItsOwnColor() {
        List<ClusterColor> colors = colors(styles.assign(clustersOfSizes(2, 3, 2, 5, 2, 4)));

        assertEquals(numbered(0, 1, 2, 3, 4, 5), colors);
    }

    @Test
    @DisplayName("요소가 하나뿐인 무리는 회색이고, 그다음 무리의 색 번호를 건너뛰게 하지 않는다")
    void leavesSingleElementClustersGrayWithoutUsingColor() {
        List<ClusterColor> colors = colors(styles.assign(clustersOfSizes(3, 1, 2, 1, 4)));

        assertEquals(List.of(ClusterColor.numbered(0), GRAY, ClusterColor.numbered(1), GRAY, ClusterColor.numbered(2)), colors);
    }

    @Test
    @DisplayName("모양은 홀로 있는 요소까지 세어 삼각기둥, 육각기둥, 원기둥의 순서로 돌려 쓴다")
    void assignsShapesInOrderIncludingSingles() {
        List<PillarShape> shapes = styles.assign(clustersOfSizes(2, 1, 2, 1)).stream().map(ClusterStyle::shape).toList();

        List<PillarShape> expected = List.of(PillarShape.TRIANGULAR_PRISM, PillarShape.HEXAGONAL_PRISM,
                PillarShape.CYLINDER, PillarShape.TRIANGULAR_PRISM);
        assertEquals(expected, shapes);
    }

    private List<ClusterColor> colors(List<ClusterStyle> assigned) {
        return assigned.stream().map(ClusterStyle::color).toList();
    }

    private List<ClusterColor> numbered(Integer... numbers) {
        return Arrays.stream(numbers).map(ClusterColor::numbered).toList();
    }

    private List<Cluster> clustersOfSizes(Integer... sizes) {
        Element element = new Element("e", Scale.MEDIUM, false, Optional.empty());
        return Arrays.stream(sizes).map(size -> new Cluster(Collections.nCopies(size, element))).toList();
    }
}
